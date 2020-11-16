package com.sales.tracker.repositories;

import com.sales.tracker.entity.ExpenseMaster;
import com.sales.tracker.entity.ReimbursementExpenseMapping;
import com.sales.tracker.entity.ReimbursementMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReimbursementExpenseMappingRepository extends JpaRepository<ReimbursementExpenseMapping, Long> {

    List<ReimbursementExpenseMapping> findByReimbursementMaster(ReimbursementMaster reimbursementMaster);

    List<ReimbursementExpenseMapping> findByExpenseMaster(ExpenseMaster expenseMaster);
}
