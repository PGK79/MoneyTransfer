package ru.netology.moneytransferservice.services;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.exceptions.TransferException;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.Logger;
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
    private Logger logger;

    public TransferService(TemporaryRepository temporaryRepository) {
        this.temporaryRepository = temporaryRepository;
    }

    public Confirmer executeTransfer(Transferer transferer) {
        String cardFromNumber = transferer.getCardFromNumber();
        String cardToNumber = transferer.getCardToNumber();
        long transferAmount = transferer.getAmount().getValue();
        logger = new Logger(cardFromNumber,cardToNumber, transferer.getAmount());

        cardChecker(cardFromNumber, cardToNumber);

        compareBalanceWithTransfer(temporaryRepository.getCardBalance(cardFromNumber),
                transferAmount);

        if (transfer(cardFromNumber, cardToNumber, transferAmount)) {
            logFile(logger,"УСПЕШНО");
            return new Confirmer(String.valueOf(counter.getAndIncrement()),
                    transferer.getCardFromCVV());
        } else {
            logFile(logger,"Error transfer");
            throw new TransferException("Операция перевода не может быть выполнена(Error transfer)",
                    counter.getAndIncrement());
        }
    }

    //TODO ЗАГЛУШКА
    public Confirmer confirmOperation(Confirmer confirmer) {
        return new Confirmer(confirmer.getOperationId(), "0001");
    }

    private void cardChecker(String cardFrom, String cardTo) {
        if (!temporaryRepository.mapSearch(cardFrom)) {
            logFile(logger,"ОШИБКА (Неверный номер карты 1)");
            throw new InputDataException("Проверьте правильность введения номерa карты № 1"
                    + "(Error input data)", counter.getAndIncrement());
        } else if (!temporaryRepository.mapSearch(cardTo)) {
            logFile(logger,"ОШИБКА (Неверный номер карты 2)");
            throw new InputDataException("Проверьте правильность введения номерa карты № 2"
                    + "(Error input data)", counter.getAndIncrement());
        }
    }

    private void compareBalanceWithTransfer(long balance, long transferAmount) {
        if (balance < transferAmount) {
            logFile(logger,"ОШИБКА (Недостаточно денег на карте)");
            throw new InputDataException("На карте нет достаточной суммы денег (Error input data)",
                    counter.getAndIncrement());
        }
    }

    private boolean transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        return temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount);
    }

    public synchronized void logFile(Logger logger, String result ) {
        SimpleDateFormat dateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        String data = dateNow.format(new Date());
        String log = "[" + data + "] " + "Карта списания: " + logger.getCardFromNumber()
                + " Карта зачисления: " + logger.getCardToNumber() + " " + " Cумма: "
                + logger.getAmount().getValue() + " "+ logger.getAmount().getCurrency()
                + " Комиссия: " + logger.getAmount().getValue()/100 + " "
                + logger.getAmount().getCurrency() + " Результат операции: " + result + "\n";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("logTransferFile.log",
                true))) {
            bw.write(log);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}