package com.ajw.quondam.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.Objects;

@JsonDeserialize(builder = Payment.Builder.class)
public class Payment {

    private final String idempotenceKey;

    private final String cardNumber;

    private final String value;

    public Payment(String idempotenceKey, String cardNumber, String value) {
        this.idempotenceKey = idempotenceKey;
        this.cardNumber = cardNumber;
        this.value = value;
    }

    public Boolean isPaid() {
        return true;
    }


    public String getIdempotenceKey() {
        return idempotenceKey;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getValue() {
        return value;
    }

    @JsonPOJOBuilder
    static class Builder {
        String idempotenceKey;
        String cardNumber;
        String value;

        Builder withIdempotenceKey(String idempotenceKey) {
            this.idempotenceKey = idempotenceKey;
            return this;
        }

        Builder withCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Payment build() {
            return new Payment(idempotenceKey, cardNumber, value);
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Payment other = (Payment) obj;
        return Objects.equal(this.getIdempotenceKey(), other.getIdempotenceKey())
                && Objects.equal(this.getCardNumber(), other.getCardNumber())
                && Objects.equal(this.getValue(), other.getValue());
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(
                this.getIdempotenceKey(), this.getCardNumber(), this.getValue());

    }

}
