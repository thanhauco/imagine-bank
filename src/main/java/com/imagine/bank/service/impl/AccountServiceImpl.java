package com.imagine.bank.service.impl;

import com.imagine.bank.service.AccountService;
import com.imagine.bank.domain.Account;
import com.imagine.bank.domain.Customer;
import com.imagine.bank.domain.Transaction;
import com.imagine.bank.repository.AccountRepository;
import com.imagine.bank.repository.CustomerRepository;
import com.imagine.bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    private final com.imagine.bank.service.FraudCheckService fraudCheckService;

    public AccountServiceImpl(AccountRepository accountRepository, 
                            CustomerRepository customerRepository,
                            TransactionRepository transactionRepository,
                            com.imagine.bank.service.FraudCheckService fraudCheckService) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
        this.fraudCheckService = fraudCheckService;
    }
    
    @Override
    public Account createAccount(Long customerId, BigDecimal initialDeposit) {
        if (initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial deposit cannot be negative");
        }
        
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
                
        Account account = new Account();
        account.setOwner(customer);
        account.setBalance(initialDeposit);
        account.setAccountNumber(generateAccountNumber());
        
        return accountRepository.save(account);
    }
    
    @Override
    public BigDecimal getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(Account::getBalance)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
    }
    
    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        // Anti-Fraud Check
        Transaction checkTx = new Transaction();
        checkTx.setAmount(amount);
        checkTx.setDescription("Transfer from " + fromAccountId + " to " + toAccountId);
        
        if (fraudCheckService.isSuspicious(checkTx)) {
            throw new SecurityException("Transaction Blocked: " + fraudCheckService.getFraudReason(checkTx));
        }
        
        // Anti-Fraud Check
        Transaction checkTx = new Transaction();
        checkTx.setAmount(amount);
        checkTx.setDescription("Transfer from " + fromAccountId + " to " + toAccountId);
        
        if (fraudCheckService.isSuspicious(checkTx)) {
            throw new SecurityException("Transaction Blocked: " + fraudCheckService.getFraudReason(checkTx));
        }
        
        Account source = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));
                
        Account target = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Target account not found"));
                
        if (source.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds in account: " + source.getAccountNumber());
        }
        
        // Execute Transfer
        source.setBalance(source.getBalance().subtract(amount));
        target.setBalance(target.getBalance().add(amount));
        
        accountRepository.save(source);
        accountRepository.save(target);
        
        // Record Transaction
        Transaction tx = new Transaction();
        tx.setSourceAccount(source);
        tx.setTargetAccount(target);
        tx.setAmount(amount);
        tx.setReference(UUID.randomUUID().toString());
        
        transactionRepository.save(tx);
    }
    
    private String generateAccountNumber() {
        return "IB" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
}
