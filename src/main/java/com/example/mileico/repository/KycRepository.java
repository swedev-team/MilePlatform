package com.example.mileico.repository;

import com.example.mileico.model.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KycRepository extends JpaRepository<Kyc, Long> {
}
