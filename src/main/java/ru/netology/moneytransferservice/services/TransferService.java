package ru.netology.moneytransferservice.services;

import org.springframework.stereotype.Service;
import ru.netology.moneytransferservice.models.Transferer;
import ru.netology.moneytransferservice.repositories.TemporaryRepository;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class TransferService {
    TemporaryRepository temporaryRepository;

    public TransferService(TemporaryRepository temporaryRepository) {
        this.temporaryRepository = temporaryRepository;
    }

    public String executeTransfer(Transferer transferer) {
        String cardFromNumber = transferer.getCardFromNumber();
        String cardToNumber = transferer.getCardToNumber();
        Long transferAmount;
        Long cardFromBalace;

        if (cardСhecker(cardFromNumber, cardToNumber)) {
            cardFromBalace = temporaryRepository.getСardBalance(cardFromNumber);
            transferAmount = (long) transferer.getAmount().getValue();
        } else throw new IllegalArgumentException("Error input data");

        if (!compareBalanceWithTransfer(cardFromBalace, transferAmount)) {
            throw new IllegalArgumentException("Error input data");
        }
        return transfer(cardFromNumber, cardToNumber, transferAmount);
    }

    private boolean cardСhecker(String cardFrom, String cardTo) {
        if (temporaryRepository.mapSearch(cardFrom) && temporaryRepository.mapSearch(cardTo)) {
            return true;
        } else return false;
    }

    private boolean compareBalanceWithTransfer(long balance, long transferAmount) {
        if (balance >= transferAmount) {
            return true;
        } else return false;
    }

    private String transfer(String cardFromNumber, String cardToNumber, long transferAmount) {
        AtomicLong counter = new AtomicLong(1);
        if (temporaryRepository.writeOffTheCard(cardFromNumber, transferAmount)
                && temporaryRepository.putMoneyTheCard(cardToNumber, transferAmount)) {
            counter.getAndIncrement();
            return String.valueOf(counter);
        } else throw new IllegalArgumentException("Error transfer");
    }
}
