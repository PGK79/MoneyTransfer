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
            loggerSimple.logFile(loggerSimple, "УСПЕШНО");
            if (!temporaryRepository.saveTransfers(String.valueOf(counter.get()), "0000")) {
                loggerSimple.logFile(loggerSimple, "Error saveTransferToRepo");
            }
            return new OperationIdDto(String.valueOf(counter.getAndIncrement()));
        } else {
            loggerSimple.logFile(loggerSimple, "Error transfer");
            throw new TransferException("Операция перевода не может быть выполнена(Error transfer)",
                    counter.getAndIncrement());
        }
    }

    public OperationIdDto confirmOperation(Confirmer confirmer) {
        if (!temporaryRepository.checkForTransactionRecord()) {
            loggerSimple.logFile(loggerSimple, "ОШИБКА. Информация в репозитории не доступна");
            throw new ConfirmationException("Информация в репозитории не доступна",
                    Integer.parseInt(confirmer.getOperationId()));
        }
        if (temporaryRepository.getTransferId(confirmer) &&
                temporaryRepository.getVerificationCode(confirmer)) {
            loggerSimple.logFile(loggerSimple, "Проверка кода завершена успешно");
            return new OperationIdDto(String.valueOf(confirmer.getOperationId()));
        } else {
            loggerSimple.logFile(loggerSimple, "ОШИБКА. ID операции не соответствует проверочному коду");
            throw new InputDataException("ID операции не соответствует проверочному коду",
                    counter.get());
        }
    }

    private void cardChecker(String cardFrom, String cardTo) {
        if (temporaryRepository.mapSearch(cardFrom)) {
            loggerSimple.logFile(loggerSimple, "ОШИБКА (Неверный номер карты 1)");
            throw new InputDataException("Проверьте правильность введения номерa карты № 1"
                    + "(Error input data)", counter.getAndIncrement());
        } else if (temporaryRepository.mapSearch(cardTo)) {
            loggerSimple.logFile(loggerSimple, "ОШИБКА (Неверный номер карты 2)");
            throw new InputDataException("Проверьте правильность введения номерa карты № 2"
                    + "(Error input data)", counter.getAndIncrement());
        }
    }

    private void compareBalanceWithTransfer(long balance, long transferAmount) {
        if (balance < transferAmount) {
            loggerSimple.logFile(loggerSimple, "ОШИБКА (Недостаточно денег на карте)");
            throw new InputDataException("На карте нет достаточной суммы денег (Error input data)",
                    counter.getAndIncrement());
        }
    }

    private boolean transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        return temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount);
    }
}