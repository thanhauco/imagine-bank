package com.imagine.bank.service.impl;
import com.imagine.bank.service.AccountService;
import com.imagine.bank.domain.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    // ... logic for transfer ...
    public void transfer(Long fromId, Long toId, BigDecimal amount) {
        // Implementation
    }
    public Account createAccount(Long customerId, BigDecimal initialDeposit) {
        return new Account();
    }
    public BigDecimal getBalance(String accountNumber) { return BigDecimal.TEN; }
}
