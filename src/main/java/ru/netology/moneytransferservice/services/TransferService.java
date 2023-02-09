package ru.netology.moneytransferservice.services;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exceptions.ConfirmationException;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.exceptions.TransferException;
import ru.netology.moneytransferservice.loggers.LoggerSimple;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.OperationIdDto;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.repositories.TemporaryRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransferService {
    private final TemporaryRepository temporaryRepository;
    private AtomicInteger counter = new AtomicInteger(1);
    private LoggerSimple loggerSimple;

    public TransferService(TemporaryRepository temporaryRepository) {
        this.temporaryRepository = temporaryRepository;
    }

    public OperationIdDto executeTransfer(Transferer transferer) {
        String cardFromNumber = transferer.getCardFromNumber();
        String cardToNumber = transferer.getCardToNumber();
        long transferAmount = transferer.getAmount().getValue();
        loggerSimple = new LoggerSimple(cardFromNumber, cardToNumber, transferer.getAmount());

        cardChecker(cardFromNumber, cardToNumber);

        compareBalanceWithTransfer(temporaryRepository.getCardBalance(cardFromNumber),
                transferAmount);

        if (transfer(cardFromNumber, cardToNumber, transferAmount)) {
            logFile(loggerSimple, "УСПЕШНО");
            temporaryRepository.saveTransfers(String.valueOf(counter.get()), "0000");
            return new OperationIdDto(String.valueOf(counter.getAndIncrement()));
        } else {
            logFile(loggerSimple, "Error transfer");
            throw new TransferException("Операция перевода не может быть выполнена(Error transfer)",
                    counter.getAndIncrement());
        }
    }

    public OperationIdDto confirmOperation(Confirmer confirmer) {
        if(!temporaryRepository.checkForTransactionRecord()){
            throw new ConfirmationException("Информация в репозитории не доступна",
                    Integer.parseInt(confirmer.getOperationId()));
        }
        if (temporaryRepository.getTransferId(confirmer) &&
                temporaryRepository.getVerificationCode(confirmer)) {
            return new OperationIdDto(String.valueOf(confirmer.getOperationId()));
        } else {
            throw new InputDataException("ID операции не соотвествует проверочному коду",
                    counter.get());
        }
    }

    private void cardChecker(String cardFrom, String cardTo) {
        if (!temporaryRepository.mapSearch(cardFrom)) {
            logFile(loggerSimple, "ОШИБКА (Неверный номер карты 1)");
            throw new InputDataException("Проверьте правильность введения номерa карты № 1"
                    + "(Error input data)", counter.getAndIncrement());
        } else if (!temporaryRepository.mapSearch(cardTo)) {
            logFile(loggerSimple, "ОШИБКА (Неверный номер карты 2)");
            throw new InputDataException("Проверьте правильность введения номерa карты № 2"
                    + "(Error input data)", counter.getAndIncrement());
        }
    }

    private void compareBalanceWithTransfer(long balance, long transferAmount) {
        if (balance < transferAmount) {
            logFile(loggerSimple, "ОШИБКА (Недостаточно денег на карте)");
            throw new InputDataException("На карте нет достаточной суммы денег (Error input data)",
                    counter.getAndIncrement());
        }
    }

    private boolean transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        return temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount);
    }

    public synchronized void logFile(LoggerSimple loggerSimple, String result) {
        SimpleDateFormat dateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String data = dateNow.format(new Date());
        String log = "[" + data + "] " + "Карта списания: " + loggerSimple.getCardFromNumber()
                + " Карта зачисления: " + loggerSimple.getCardToNumber() + " " + " Cумма: "
                + loggerSimple.getAmount().getValue() + " " + loggerSimple.getAmount().getCurrency()
                + " Комиссия: " + loggerSimple.getAmount().getValue() / 100 + " "
                + loggerSimple.getAmount().getCurrency() + " Результат операции: " + result + "\n";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("logs/logFile.log",
                true))) {
            bw.write(log);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}