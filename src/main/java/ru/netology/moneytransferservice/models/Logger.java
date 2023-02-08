package ru.netology.moneytransferservice.models;

import lombok.Data;

@Data
public class Logger {
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;

    public Logger(String cardFromNumber, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }
}
