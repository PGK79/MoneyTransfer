package ru.netology.moneytransferservice.exceptions;

import lombok.Data;

@Data
public class TransferException extends RuntimeException {
    private final int id;
    public TransferException(String msg, int id) {
        super(msg);
        this.id = id;
    }
}