package com.sales.tracker.repositories;


import com.sales.tracker.entity.ExpenseMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseMaster, Long> {

}
