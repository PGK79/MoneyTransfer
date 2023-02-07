package ru.netology.moneytransferservice.repositories;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TemporaryRepository {
    Map<String, Long> validCards = new ConcurrentHashMap<>(Map.of("1234123412341234", 1000L
            , "2345234523452345", 10000L, "3456345634563456", 100000L,
            "1111111111111111", 10L, "2222222222222222", 100L));

    public boolean mapSearch(String cardNumber) {
        return validCards.containsKey(cardNumber);
    }

    public Long getСardBalance(String cardNumber) {
        return validCards.get(cardNumber);
    }

    public boolean writeOffTheCard(String cardFromNumber, Long transferAmount) {
        validCards.put(cardFromNumber, validCards.get(cardFromNumber) - transferAmount);
        return true;
    }

    public boolean putMoneyTheCard(String cardToNumber, Long transferAmount){
        validCards.put(cardToNumber, validCards.get(cardToNumber) + transferAmount);
        return true;
    }
}
