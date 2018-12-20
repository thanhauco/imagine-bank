package com.imagine.bank.service;

import com.imagine.bank.domain.Investment;
import java.util.List;

public interface InvestmentService {
    List<Investment> getPortfolio(Long customerId);
    Investment buyStock(Long customerId, String symbol, int quantity);
}
