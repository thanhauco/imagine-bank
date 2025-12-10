package com.imagine.bank.service.impl;

import com.imagine.bank.domain.Loan;
import com.imagine.bank.domain.LoanStatus;
import com.imagine.bank.domain.Customer;
import com.imagine.bank.repository.LoanRepository;
import com.imagine.bank.repository.CustomerRepository;
import com.imagine.bank.service.LoanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    public LoanServiceImpl(LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Loan applyForLoan(Long customerId, BigDecimal amount, Integer termMonths) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Loan loan = new Loan();
        loan.setCustomer(customer);
        loan.setAmount(amount);
        loan.setTermMonths(termMonths);
        loan.setInterestRate(new BigDecimal("0.05")); // Fixed 5% rate for simplicity
        loan.setStatus(LoanStatus.PENDING);
        loan.setRemainingBalance(amount);
        
        // Calculate estimated monthly payment (Principal + Interest) / Term
        // Note: Simplified logic vs real amortization
        BigDecimal totalInterest = amount.multiply(loan.getInterestRate());
        BigDecimal totalDue = amount.add(totalInterest);
        BigDecimal monthly = totalDue.divide(new BigDecimal(termMonths), 2, RoundingMode.HALF_UP);
        loan.setMonthlyPayment(monthly);

        return loanRepository.save(loan);
    }

    @Override
    @Transactional
    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new IllegalStateException("Loan is not pending");
        }

        // Simulate credit check: Approve if amount < 50,000 OR Customer has > 1000 balance
        // For now, auto-approve for demonstration
        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovedAt(LocalDateTime.now());

        return loanRepository.save(loan);
    }

    @Override
    @Transactional
    public Loan rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        
        loan.setStatus(LoanStatus.REJECTED);
        return loanRepository.save(loan);
    }

    @Override
    @Transactional
    public void makePayment(Long loanId, BigDecimal amount) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() != LoanStatus.APPROVED) {
            throw new IllegalStateException("Loan is not active");
        }

        BigDecimal newBalance = loan.getRemainingBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            newBalance = BigDecimal.ZERO;
            loan.setStatus(LoanStatus.PAID_OFF);
        }
        
        loan.setRemainingBalance(newBalance);
        loanRepository.save(loan);
    }

    @Override
    public List<Loan> getCustomerLoans(Long customerId) {
        return loanRepository.findByCustomerId(customerId);
    }
}
