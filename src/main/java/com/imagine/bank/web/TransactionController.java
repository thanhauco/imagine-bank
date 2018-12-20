package com.imagine.bank.web;

import com.imagine.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import lombok.Data;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    
    private final AccountService accountService;
    
    public TransactionController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        try {
            accountService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount()
            );
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @Data
    static class TransferRequest {
        private Long fromAccountId;
        private Long toAccountId;
        private BigDecimal amount;
    }
}
