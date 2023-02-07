package ru.netology.moneytransferservice.repositories;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TemporaryRepository {
    Map<String, Long> validCards = Map.of("1234_1234_1234_1234", 1000L
            , "2345_2345_2345_2345", 10000L, "3456_3456_3456_3456", 100000L,
            "1111_1111_1111_1111", 10L);

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
