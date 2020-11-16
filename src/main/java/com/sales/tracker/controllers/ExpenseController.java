package com.sales.tracker.controllers;

import com.sales.tracker.models.Expense;
import com.sales.tracker.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<List<Expense>> getAll() {
        return new ResponseEntity<List<Expense>>(expenseService.getAllExpenses(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<Optional<Expense>> getExpense(@PathVariable Long id) {
        return new ResponseEntity<Optional<Expense>>(expenseService.getExpense(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Expense> create(@RequestBody final Expense expense) {
        return new ResponseEntity<Expense>(expenseService.createExpense(expense), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Expense> update(@RequestBody final Expense expense) {
        return new ResponseEntity<Expense>(expenseService.updateExpense(expense), HttpStatus.OK);
    }

    @DeleteMapping
    public void delete(@PathVariable final Long id) {
        expenseService.deleteExpense(id);
    }
}