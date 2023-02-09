package ru.netology.moneytransferservice.repositories;

import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.models.Confirmer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TemporaryRepository {
    Map<String, Long> validCards = new ConcurrentHashMap<>(Map.of("1234123412341234", 1000L
            , "2345234523452345", 10000L, "3456345634563456", 100000L,
            "1111111111111111", 1000000L, "2222222222222222", 100L));

    Map<String, String> transfers = new ConcurrentHashMap<>();

    public boolean mapSearch(String cardNumber) {
        return validCards.containsKey(cardNumber);
    }

    public long getCardBalance(String cardNumber) {
        if(mapSearch(cardNumber)){
        return validCards.get(cardNumber);
        }else return 0;
    }

    public boolean writeOffTheCard(String cardFromNumber, Long transferAmount) {
        validCards.replace(cardFromNumber, validCards.get(cardFromNumber) - transferAmount);
        return true;
    }

    public boolean putMoneyTheCard(String cardToNumber, Long transferAmount) {
        validCards.replace(cardToNumber, validCards.get(cardToNumber) + transferAmount);
        return true;
    }

    public boolean saveTransfers(String operationId, String code) {
        transfers.putIfAbsent(operationId, code);
        return true;
    }

    public boolean getTransferId(Confirmer confirmer) {
        return transfers.containsKey(confirmer.getOperationId());
    }

    public boolean getVerificationCode(Confirmer confirmer) {
        return transfers.get(confirmer.getOperationId()).equals(confirmer.getCode());
    }

    public boolean checkForTransactionRecord() {
        return transfers.isEmpty();
    }
}