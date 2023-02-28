package ru.netology.moneytransferservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.netology.moneytransferservice.logger.LoggerSimple;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfirmationException extends RuntimeException {
    private final int id;
    private final LoggerSimple loggerSimple;

    public ConfirmationException(String msg, int id, LoggerSimple loggerSimple) {
        super(msg);
        this.id = id;
        this.loggerSimple = loggerSimple;
    }
}