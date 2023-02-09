package ru.netology.moneytransferservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Confirmer {
    private  String operationId;
    private  String code;
}