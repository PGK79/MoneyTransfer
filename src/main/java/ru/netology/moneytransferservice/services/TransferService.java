package ru.netology.moneytransferservice.services;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.exceptions.TransferException;
import ru.netology.moneytransferservice.models.Confirmer;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.repositories.TemporaryRepository;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TransferService {
    private final TemporaryRepository temporaryRepository;
    private AtomicInteger counter = new AtomicInteger(1);

    public TransferService(TemporaryRepository temporaryRepository) {
        this.temporaryRepository = temporaryRepository;
    }

    public Confirmer executeTransfer(Transferer transferer) {
        String cardFromNumber = transferer.getCardFromNumber();
        String cardToNumber = transferer.getCardToNumber();
        long transferAmount = transferer.getAmount().getValue();

        cardChecker(cardFromNumber, cardToNumber);

        compareBalanceWithTransfer(temporaryRepository.getCardBalance(cardFromNumber),
                transferAmount);

        if (transfer(cardFromNumber, cardToNumber, transferAmount)) {
            return new Confirmer(String.valueOf(counter.getAndIncrement()),
                    transferer.getCardFromCVV());
        } else {
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
            throw new InputDataException("Проверьте правильность введения номерa карты № 1"
                    + "(Error input data)", counter.getAndIncrement());
        } else if (!temporaryRepository.mapSearch(cardTo)) {
            throw new InputDataException("Проверьте правильность введения номерa карты № 2"
                    + "(Error input data)", counter.getAndIncrement());
        }

    }

    private void compareBalanceWithTransfer(long balance, long transferAmount) {
        if (balance < transferAmount) {
            throw new InputDataException("На карте нет достаточной суммы денег (Error input data)",
                    counter.getAndIncrement());
        }
    }

    private boolean transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        return temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount);
    }
}