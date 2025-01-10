package com.example.bankingapi.controller;

import com.example.bankingapi.dto.PaymentRequest;
import com.example.bankingapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    // Inject the PaymentService into the controller
    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-payment-intent")
    public String createPaymentIntent(@RequestBody PaymentRequest paymentRequest) {
        // Delegate the logic to PaymentService
        return paymentService.createPaymentIntent(paymentRequest);
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test successful!";
    }

}
