package ru.netology.moneytransferservice.models;

import java.util.Objects;

public class Confirmer {
    private final String operationId;
    private final transient String code;

    public Confirmer(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Confirmer confirmer = (Confirmer) o;
        return Objects.equals(operationId, confirmer.operationId) && Objects.equals(code, confirmer.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, code);
    }

    @Override
    public String toString() {
        return "Confirmer{" +
                "operationId='" + operationId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}