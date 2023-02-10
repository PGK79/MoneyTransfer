package ru.netology.moneytransferservice.exceptions;

import lombok.Data;
import ru.netology.moneytransferservice.loggers.LoggerSimple;

@Data
public class InputDataException extends RuntimeException {
    private final int id;
    private final LoggerSimple loggerSimple;

    public InputDataException(String msg, int id, LoggerSimple logger) {
        super(msg);
        this.id = id;
        this.loggerSimple = logger;
    }
}