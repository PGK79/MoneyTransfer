package ru.netology.moneytransferservice.exception;

import lombok.Data;
import ru.netology.moneytransferservice.logger.LoggerSimple;

@Data
public class TransferException extends RuntimeException {
    private final int id;
    private final LoggerSimple logger;

    public TransferException(String msg, int id, LoggerSimple logger) {
        super(msg);
        this.id = id;
        this.logger = logger;
    }
}