package com.sales.tracker.repositories;

import com.sales.tracker.entity.ReimbursementMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReimbursementRepository extends JpaRepository<ReimbursementMaster, Long> {
}
