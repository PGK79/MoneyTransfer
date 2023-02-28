package ru.netology.moneytransferservice.model;

import lombok.Data;

@Data
public class Amount {
    private final long value;
    private final String currency;
}