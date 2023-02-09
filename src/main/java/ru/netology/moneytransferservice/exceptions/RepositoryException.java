package ru.netology.moneytransferservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RepositoryException extends RuntimeException {
    private final int id;

    public RepositoryException(String msg, int id) {
        super(msg);
        this.id = id;
    }
}
