package ru.netology.moneytransferservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfirmationException extends RuntimeException {
    private final int id;

    public ConfirmationException(String msg, int id) {
        super(msg);
        this.id = id;
    }
}
