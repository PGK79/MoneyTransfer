package ru.netology.moneytransferservice.exceptions;

public class TransferException extends RuntimeException {
    public TransferException(String msg) {
        super(msg);
    }
}