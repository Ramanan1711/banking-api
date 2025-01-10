package com.example.bankingapi.service;

import com.example.bankingapi.dto.PaymentRequest;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public String createPaymentIntent(PaymentRequest paymentRequest) {
        try {
            // Create PaymentIntent parameters
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(paymentRequest.getAmount())  // Amount in cents (e.g., 1000 = $10.00)
                    .setCurrency("usd")  // Currency in which the payment will be processed
                    .setPaymentMethod(paymentRequest.getPaymentMethodId())  // Payment method ID from frontend
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setConfirm(true)  // Automatically confirm the payment
                    .build();

            // Create the PaymentIntent object
            PaymentIntent intent = PaymentIntent.create(params);

            // Return the client secret to the frontend
            return intent.getClientSecret();
        } catch (Exception e) {
            // Log the exception (You can implement a logger here)
            System.err.println("Error creating payment intent: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    // You can add other methods to confirm payments, handle refunds, etc.
}
