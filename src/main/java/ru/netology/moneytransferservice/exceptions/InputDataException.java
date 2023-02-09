package ru.netology.moneytransferservice.exceptions;

import lombok.Data;
import ru.netology.moneytransferservice.loggers.LoggerSimple;

@Data
public class InputDataException extends RuntimeException {
    private final int id;

    public InputDataException(String msg, int id) {
        super(msg);
        this.id = id;
    }
    public InputDataException(String msg, int id, LoggerSimple logger) {
        super(msg);
        this.id = id;
        logger.logFile(msg);
    }
}