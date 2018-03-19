package com.example.mileico.repository;

import com.example.mileico.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findByIsChecked(Boolean isChecked);
    List<Transactions> findBySender(String sender);
}
