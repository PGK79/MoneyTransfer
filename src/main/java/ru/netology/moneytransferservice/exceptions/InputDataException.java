package ru.netology.moneytransferservice.exceptions;

public class InputDataException extends RuntimeException {
    public InputDataException(String msg) {
        super(msg);
    }
}