package com.example.bankingapi.service;

import com.example.bankingapi.entity.Account;
import com.example.bankingapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account accountDetails) {
        Account account = getAccountById(id);
        account.setAccountHolderName(accountDetails.getAccountHolderName());
        account.setBalance(accountDetails.getBalance());
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }

    public long getAccountCount() {
        return accountRepository.count();
    }

    public String transfer(Long fromAccountId, Long toAccountId, Double amount) {
        // Fetch source account
        Account fromAccount = getAccountById(fromAccountId);

        // Fetch destination account
        Account toAccount = getAccountById(toAccountId);

        // Validate transfer
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }
        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance in the source account.");
        }

        // Perform the transfer
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        // Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return "Transfer of " + amount + " from account ID " + fromAccountId + " to account ID " + toAccountId + " completed successfully.";
    }
}
