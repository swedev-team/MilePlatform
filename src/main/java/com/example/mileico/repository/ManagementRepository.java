package com.example.mileico.repository;

import com.example.mileico.model.MileManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementRepository extends JpaRepository<MileManagement, Long> {
    MileManagement findByIsProgress(boolean isProgress);
    MileManagement findByIdx(Long idx);
}
