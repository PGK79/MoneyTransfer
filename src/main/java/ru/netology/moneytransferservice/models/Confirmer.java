package ru.netology.moneytransferservice.models;

import lombok.Data;

@Data
public class Confirmer {
    private final String operationId;
    private final String code;
}