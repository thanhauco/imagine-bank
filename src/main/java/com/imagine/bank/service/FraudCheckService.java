package com.imagine.bank.service;

import com.imagine.bank.domain.Transaction;

public interface FraudCheckService {
    boolean isSuspicious(Transaction transaction);
    String getFraudReason(Transaction transaction);
}
