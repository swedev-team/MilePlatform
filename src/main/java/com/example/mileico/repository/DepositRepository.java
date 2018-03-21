package com.example.mileico.repository;

import com.example.mileico.model.Deposit;
import com.example.mileico.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findByUser(User user);
}
