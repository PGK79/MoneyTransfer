package ru.netology.moneytransferservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.netology.moneytransferservice.loggers.LoggerSimple;

@EqualsAndHashCode(callSuper = true)
@Data
public class RepositoryException extends RuntimeException {
    private final int id;
    private final LoggerSimple loggerSimple;

    public RepositoryException(String msg, int id, LoggerSimple loggerSimple) {
        super(msg);
        this.id = id;
        this.loggerSimple = loggerSimple;
    }
}