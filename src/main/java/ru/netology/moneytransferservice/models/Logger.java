package ru.netology.moneytransferservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Logger {
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;
}
