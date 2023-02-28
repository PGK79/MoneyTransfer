package ru.netology.moneytransferservice.service;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exception.ConfirmationException;
import ru.netology.moneytransferservice.exception.InputDataException;
import ru.netology.moneytransferservice.exception.RepositoryException;
import ru.netology.moneytransferservice.exception.TransferException;
import ru.netology.moneytransferservice.logger.LoggerSimple;
import ru.netology.moneytransferservice.model.Confirmer;
import ru.netology.moneytransferservice.model.OperationIdDto;
import ru.netology.moneytransferservice.model.Transferer;
import ru.netology.moneytransferservice.repository.TemporaryRepository;

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

        cardChecker(cardFromNumber, cardToNumber, loggerSimple);

        compareBalanceWithTransfer(temporaryRepository.getCardBalance(cardFromNumber),
                transferAmount, loggerSimple);

        if (transfer(cardFromNumber, cardToNumber, transferAmount)) {
            loggerSimple.logFile("УСПЕШНО");
            if (!temporaryRepository.saveTransfers(String.valueOf(counter.get()), "0000")) {
                throw new RepositoryException("Error saveTransferToRepo", counter.get(), loggerSimple);
            }
            return new OperationIdDto(String.valueOf(counter.getAndIncrement()));
        } else {
            throw new TransferException("Операция перевода не может быть выполнена",
                    counter.getAndIncrement(), loggerSimple);
        }
    }

    public OperationIdDto confirmOperation(Confirmer confirmer) {
        if (temporaryRepository.checkForTransactionRecord()) {
            throw new ConfirmationException("Информация в репозитории не доступна",
                    Integer.parseInt(confirmer.getOperationId()), loggerSimple);
        }
        if (temporaryRepository.getTransferId(confirmer) &&
                temporaryRepository.getVerificationCode(confirmer)) {
            return new OperationIdDto(String.valueOf(confirmer.getOperationId()));
        } else {
            throw new InputDataException("ID операции не соответствует проверочному коду",
                    counter.get(), loggerSimple);
        }
    }

    public void cardChecker(String cardFrom, String cardTo, LoggerSimple loggerSimple) {
        if (!temporaryRepository.mapSearch(cardFrom)) {
            throw new InputDataException("ОШИБКА (Неверный номер карты 1)", counter.getAndIncrement(),
                    loggerSimple);
        } else if (!temporaryRepository.mapSearch(cardTo)) {
            throw new InputDataException("ОШИБКА (Неверный номер карты 2)",
                    counter.getAndIncrement(), loggerSimple);
        }
    }

    public void compareBalanceWithTransfer(long balance, long transferAmount, LoggerSimple loggerSimple) {
        if (balance < transferAmount) {
            throw new InputDataException("На карте нет достаточной суммы денег",
                    counter.getAndIncrement(), loggerSimple);
        }
    }

    public boolean transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        return temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount);
    }
}