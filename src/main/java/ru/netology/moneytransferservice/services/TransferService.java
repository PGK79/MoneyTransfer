package ru.netology.moneytransferservice.services;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.exceptions.TransferException;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.repositories.TemporaryRepository;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransferService {
    private final TemporaryRepository temporaryRepository;

    public TransferService(TemporaryRepository temporaryRepository) {
        this.temporaryRepository = temporaryRepository;
    }

    public String executeTransfer(Transferer transferer) {
        String cardFromNumber = transferer.getCardFromNumber();
        String cardToNumber = transferer.getCardToNumber();
        long transferAmount;
        long cardFromBalance;

        if (cardChecker(cardFromNumber, cardToNumber)) {
            cardFromBalance = temporaryRepository.getCardBalance(cardFromNumber);
            transferAmount = transferer.getAmount().getValue();
        } else throw new InputDataException("Error input data");

        if (!compareBalanceWithTransfer(cardFromBalance, transferAmount)) {
            throw new InputDataException("Error input data");
        }
        return transfer(cardFromNumber, cardToNumber, transferAmount);
    }

    private boolean cardChecker(String cardFrom, String cardTo) {
        return temporaryRepository.mapSearch(cardFrom) && temporaryRepository.mapSearch(cardTo);
    }

    private boolean compareBalanceWithTransfer(long balance, long transferAmount) {
        return balance >= transferAmount;
    }

    private String transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        AtomicLong counter = new AtomicLong(1);
        if (temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount)) {
            counter.getAndIncrement();
            return String.valueOf(counter);
        } else throw new TransferException("Error transfer");
    }
}