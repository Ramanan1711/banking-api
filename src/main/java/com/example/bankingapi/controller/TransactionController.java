package com.example.bankingapi.controller;

import com.example.bankingapi.entity.Transaction;
import com.example.bankingapi.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Get all transactions for a specific account
    @GetMapping("/{accountId}")
    public List<Transaction> getTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    // Deposit money into an account (accountId and amount in the body)
    @PostMapping("/deposit")
    public Transaction deposit(@RequestBody TransactionRequest depositRequest) {
        return transactionService.deposit(depositRequest.getAccountId(), depositRequest.getAmount());
    }

    // Withdraw money from an account (accountId and amount in the body)
    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestBody TransactionRequest withdrawRequest) {
        return transactionService.withdraw(withdrawRequest.getAccountId(), withdrawRequest.getAmount());
    }

    // Create a transaction (manual, not related to deposit/withdraw)
    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    // Get transaction by transaction ID (optional)
    @GetMapping("/transaction/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/monthly")
    public List<Map<String, Object>> getMonthlyTransactions() {
        return transactionService.getMonthlyTransactions();
    }
}
