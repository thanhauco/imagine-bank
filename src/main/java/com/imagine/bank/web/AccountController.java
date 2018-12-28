package com.imagine.bank.web;

import com.imagine.bank.service.AccountService;
import com.imagine.bank.domain.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    private final AccountService service;
    
    public AccountController(AccountService service) { 
        this.service = service; 
    }
    
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String accountNumber) {
        return ResponseEntity.ok(service.getBalance(accountNumber));
    }
    
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestParam Long customerId, 
                                               @RequestParam BigDecimal initialDeposit) {
        return ResponseEntity.ok(service.createAccount(customerId, initialDeposit));
    }
}
