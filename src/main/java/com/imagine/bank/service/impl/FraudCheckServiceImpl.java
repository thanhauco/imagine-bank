package com.imagine.bank.service.impl;

import com.imagine.bank.domain.Transaction;
import com.imagine.bank.service.FraudCheckService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class FraudCheckServiceImpl implements FraudCheckService {

    private static final BigDecimal THRESHOLD = new BigDecimal("10000.00");

    @Override
    public boolean isSuspicious(Transaction transaction) {
        // Rule 1: Amount > 10,000
        if (transaction.getAmount().compareTo(THRESHOLD) > 0) {
            return true;
        }

        // Rule 2: Description contains "Overseas" or "Crypto" (Simulated)
        String desc = transaction.getDescription().toLowerCase();
        if (desc.contains("overseas") || desc.contains("crypto")) {
            return true;
        }

        return false;
    }

    @Override
    public String getFraudReason(Transaction transaction) {
        if (transaction.getAmount().compareTo(THRESHOLD) > 0) {
            return "Transaction amount exceeds limit ($10,000)";
        }
        if (transaction.getDescription().toLowerCase().contains("overseas")) {
            return "Suspicious international transfer";
        }
        if (transaction.getDescription().toLowerCase().contains("crypto")) {
            return "High-risk merchant category";
        }
        return "Unknown";
    }
}
