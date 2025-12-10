package com.imagine.bank.service;

import com.imagine.bank.domain.Loan;
import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    Loan applyForLoan(Long customerId, BigDecimal amount, Integer termMonths);
    Loan approveLoan(Long loanId);
    Loan rejectLoan(Long loanId);
    void makePayment(Long loanId, BigDecimal amount);
    List<Loan> getCustomerLoans(Long customerId);
}
