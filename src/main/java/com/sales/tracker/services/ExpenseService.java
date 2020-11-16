package com.sales.tracker.services;

import com.sales.tracker.entity.ExpenseMaster;
import com.sales.tracker.models.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    List<Expense> getAllExpenses();

    Optional<Expense> getExpense(Long id);

    Expense createExpense(Expense expense);

    Expense updateExpense(Expense expense);

    void deleteExpense(Long id);

}
