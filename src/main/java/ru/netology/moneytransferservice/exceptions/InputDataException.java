package ru.netology.moneytransferservice.exceptions;

import lombok.Data;

@Data
public class InputDataException extends RuntimeException {
    private final int id;
    public InputDataException(String msg, int id) {
        super(msg);
        this.id = id;
    }
}