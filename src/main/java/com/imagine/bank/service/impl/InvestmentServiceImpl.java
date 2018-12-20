package com.imagine.bank.service.impl;

import com.imagine.bank.service.InvestmentService;
import com.imagine.bank.domain.Investment;
import com.imagine.bank.repository.InvestmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InvestmentServiceImpl implements InvestmentService {
    
    private final InvestmentRepository investmentRepository;
    
    public InvestmentServiceImpl(InvestmentRepository investmentRepository) {
        this.investmentRepository = investmentRepository;
    }
    
    @Override
    public List<Investment> getPortfolio(Long customerId) {
        // In real app, would filter by customer. For now return all for demo
        return investmentRepository.findAll();
    }
    
    @Override
    public Investment buyStock(Long customerId, String symbol, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        
        // Simulating logic: Check external market price (mocked)
        // Deduct funds from account (skipped for brevity unless we inject AccountService)
        
        Investment inv = new Investment();
        inv.setSymbol(symbol.toUpperCase());
        inv.setQuantity(quantity);
        // inv.setOwner(customer); // If we added linkage
        
        return investmentRepository.save(inv);
    }
}
