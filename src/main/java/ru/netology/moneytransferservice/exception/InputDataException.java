package ru.netology.moneytransferservice.exception;

import lombok.Data;
import ru.netology.moneytransferservice.logger.LoggerSimple;

import java.util.Objects;

@Data
public class InputDataException extends RuntimeException {
    private final int id;
    private final LoggerSimple loggerSimple;

    public InputDataException(String msg, int id, LoggerSimple logger) {
        super(msg);
        this.id = id;
        this.loggerSimple = logger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputDataException that = (InputDataException) o;
        return id == that.id && Objects.equals(loggerSimple, that.loggerSimple);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, loggerSimple);
    }
}