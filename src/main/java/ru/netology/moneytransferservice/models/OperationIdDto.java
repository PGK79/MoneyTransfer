package ru.netology.moneytransferservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OperationIdDto {
    private final String operationId;
}