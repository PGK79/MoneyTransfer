package ru.netology.moneytransferservice.models;

import java.util.Objects;

public class Transferer {
    private final String cardFromNumber;
    private final String cardFromValidTill;
    private final String cardFromCVV;
    private final String cardToNumber;
    private final Amount amount;

        public Transferer(String cardFromNumber, String cardFromValidTill, String cardFromCVV,
                      String cardToNumber,Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTill = cardFromValidTill;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transferer that = (Transferer) o;
        return Objects.equals(cardFromNumber, that.cardFromNumber) && Objects.equals(cardFromValidTill, that.cardFromValidTill) && Objects.equals(cardFromCVV, that.cardFromCVV) && Objects.equals(cardToNumber, that.cardToNumber) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardFromNumber, cardFromValidTill, cardFromCVV, cardToNumber, amount);
    }

    @Override
    public String toString() {
        return "Transferer{" +
                "cardFromNumber='" + cardFromNumber + '\'' +
                ", cardFromValidTill='" + cardFromValidTill + '\'' +
                ", cardFromCVV='" + cardFromCVV + '\'' +
                ", cardToNumber='" + cardToNumber + '\'' +
                ", amount=" + amount +
                '}';
    }
}