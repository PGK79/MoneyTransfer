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
        long transferAmount;
        long cardFromBalance;

        if (cardChecker(cardFromNumber, cardToNumber)) {
            cardFromBalance = temporaryRepository.getCardBalance(cardFromNumber);
            transferAmount = transferer.getAmount().getValue();
        } else {
            throw new InputDataException("Проверьте правильность введения номеров карт (Error input data)",
                    counter.getAndIncrement());
       }

        if (!compareBalanceWithTransfer(cardFromBalance, transferAmount)) {
            throw new InputDataException("На карте нет достаточной суммы денег (Error input data)",
                    counter.getAndIncrement());
        }
        if(transfer(cardFromNumber, cardToNumber, transferAmount)){
            return new Confirmer(String.valueOf(counter.getAndIncrement()),
                    transferer.getCardFromCVV());
        }else {
            counter.getAndIncrement();
            throw new TransferException("Операция перевода не может быть выполнена(Error transfer)",
                    counter.get());
        }
    }

    //TODO ЗАГЛУШКА
    public Confirmer confirmOperation(Confirmer confirmer){
        return new Confirmer(confirmer.getOperationId(), "0001");
    }

    private boolean cardChecker(String cardFrom, String cardTo) {
        return temporaryRepository.mapSearch(cardFrom) && temporaryRepository.mapSearch(cardTo);
    }

    private boolean compareBalanceWithTransfer(long balance, long transferAmount) {
        return balance >= transferAmount;
    }

    private boolean transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        return temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount);
    }
}