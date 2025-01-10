package com.example.bankingapi.dto;

public class PaymentRequest {
    private String paymentMethodId; // Payment method ID
    private long amount;  // Amount in cents (e.g., 1000 = $10.00)

    // Getter for paymentMethodId
    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    // Setter for paymentMethodId
    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    // Getter for amount
    public long getAmount() {
        return amount;
    }

    // Setter for amount
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
