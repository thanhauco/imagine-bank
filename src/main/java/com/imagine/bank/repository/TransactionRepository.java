package com.imagine.bank.repository;
import com.imagine.bank.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {}
