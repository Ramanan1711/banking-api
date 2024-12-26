package com.example.bankingapi.controller;

import com.example.bankingapi.dto.TransferRequest;
import com.example.bankingapi.entity.Account;
import com.example.bankingapi.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
        return accountService.updateAccount(id, accountDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return "Account with ID " + id + " has been deleted.";
    }

    @GetMapping("/count")
    public long getAccountCount() {
        return accountService.getAccountCount();
    }

    @PostMapping("/transfer")
    public String transfer(@RequestBody TransferRequest transferRequest) {
        return accountService.transfer(
                transferRequest.getFromAccountId(),
                transferRequest.getToAccountId(),
                transferRequest.getAmount()
        );
    }
}
