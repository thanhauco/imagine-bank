package com.imagine.bank.web;

import com.imagine.bank.domain.Loan;
import com.imagine.bank.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/apply")
    public ResponseEntity<Loan> apply(@RequestBody Map<String, Object> request) {
        Long customerId = Long.valueOf(request.get("customerId").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        Integer termMonths = Integer.valueOf(request.get("termMonths").toString());

        return ResponseEntity.ok(loanService.applyForLoan(customerId, amount, termMonths));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Loan> approve(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.approveLoan(id));
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> pay(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        loanService.makePayment(id, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Loan>> getCustomerLoans(@PathVariable Long customerId) {
        return ResponseEntity.ok(loanService.getCustomerLoans(customerId));
    }
}
