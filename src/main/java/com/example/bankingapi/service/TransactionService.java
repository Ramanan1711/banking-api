package com.example.bankingapi.service;

import com.example.bankingapi.entity.Account;
import com.example.bankingapi.entity.Transaction;
import com.example.bankingapi.repository.AccountRepository;
import com.example.bankingapi.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    // Correct constructor to initialize both repositories
    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;  // Now properly initialized
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction deposit(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType("CREDIT");
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(Long accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + accountId));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance for withdrawal.");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType("DEBIT");
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }


    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
    }

    public List<Map<String, Object>> getMonthlyTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        // Group transactions by month and type (CREDIT/DEBIT)
        return transactions.stream()
                .filter(transaction -> transaction.getTimestamp() != null)
                .collect(Collectors.groupingBy(transaction -> transaction.getTimestamp().getMonth().toString() + " " + transaction.getTimestamp().getYear()))
                .entrySet().stream()
                .map(entry -> {
                    String month = entry.getKey();
                    Double creditAmount = entry.getValue().stream()
                            .filter(t -> t.getType().equals("CREDIT"))
                            .mapToDouble(Transaction::getAmount)
                            .sum();
                    Double debitAmount = entry.getValue().stream()
                            .filter(t -> t.getType().equals("DEBIT"))
                            .mapToDouble(Transaction::getAmount)
                            .sum();

                    // Use HashMap to create a map with the correct type
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("month", month);
                    resultMap.put("CREDIT", creditAmount);
                    resultMap.put("DEBIT", debitAmount);
                    return resultMap;
                })
                .collect(Collectors.toList());
    }


}
