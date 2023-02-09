package ru.netology.moneytransferservice.logs;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.moneytransferservice.models.Amount;

@Data
@AllArgsConstructor
public class Logger {
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;
}
