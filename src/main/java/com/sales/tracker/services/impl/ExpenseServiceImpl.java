package com.sales.tracker.services.impl;

import com.sales.tracker.entity.ExpenseMaster;
import com.sales.tracker.models.Expense;
import com.sales.tracker.repositories.ExpenseRepository;
import com.sales.tracker.services.ExpenseService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MapperFacade mapper;

    @Override
    public List<Expense> getAllExpenses() {
        List<Expense> expenses = expenseRepository.findAll().stream().map(r -> mapper.map(r, Expense.class)).collect(Collectors.toList());
        return expenses;
    }

    @Override
    public Optional<Expense> getExpense(Long id) {
        return expenseRepository.findById(id).map(r -> mapper.map(r, Expense.class));
    }

    @Override
    public Expense createExpense(Expense expense) {
        ExpenseMaster expenseMaster = expenseRepository.save(mapper.map(expense, ExpenseMaster.class));
        return mapper.map(expenseMaster, Expense.class);
    }

    @Override
    public Expense updateExpense(Expense expense) {
        ExpenseMaster expenseMaster = expenseRepository.save(mapper.map(expense, ExpenseMaster.class));
        return mapper.map(expenseMaster, Expense.class);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
