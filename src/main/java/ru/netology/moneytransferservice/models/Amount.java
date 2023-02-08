package ru.netology.moneytransferservice.models;

import lombok.Data;

@Data
public class Amount {
    private final long value;
    private final String currency;
}