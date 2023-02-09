package ru.netology.moneytransferservice.loggers;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.netology.moneytransferservice.models.Amount;

@Data
@AllArgsConstructor
public class LoggerSimple {
    private String cardFromNumber;
    private String cardToNumber;
    private Amount amount;
}
