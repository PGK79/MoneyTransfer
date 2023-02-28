package ru.netology.moneytransferservice.model;

import lombok.Data;

@Data
public class ExceptionDto {
    private final String message;
    private final int id;
}