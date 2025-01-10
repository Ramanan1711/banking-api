package com.example.bankingapi.controller;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            System.out.println("Received payload: " + payload);  // For debugging

            // Verify and construct the event from the payload and signature header
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("Event type: " + event.getType());  // For debugging

            switch (event.getType()) {
                case "payment_intent.succeeded":
                    // Handle successful payment
                    break;
                case "payment_intent.payment_failed":
                    // Handle payment failure
                    break;
                default:
                    // Handle other event types
                    break;
            }

            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
