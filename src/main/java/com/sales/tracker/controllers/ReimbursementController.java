package com.sales.tracker.controllers;

import com.sales.tracker.models.Reimbursement;
import com.sales.tracker.services.ReimbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reimbursement")
public class ReimbursementController {

    @Autowired
    private ReimbursementService reimbursementService;

    @GetMapping
    public ResponseEntity<List<Reimbursement>> getAll() {
        return new ResponseEntity<List<Reimbursement>>(reimbursementService.getAllReimbursement(), HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping("{id}")
    public ResponseEntity<Optional<Reimbursement>> getReimbursement(@PathVariable Long id) {
        return new ResponseEntity<Optional<Reimbursement>>(reimbursementService.getReimbursement(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Reimbursement> create(@RequestBody final Reimbursement reimbursement) {
        return new ResponseEntity<Reimbursement>(reimbursementService.createReimbursement(reimbursement), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Reimbursement> update(@RequestBody final Reimbursement reimbursement) {
        return new ResponseEntity<Reimbursement>(reimbursementService.updateReimbursement(reimbursement), HttpStatus.OK);
    }

    @DeleteMapping
    public void delete(@PathVariable final Long id) {

        reimbursementService.deleteReimbursement(id);
    }
}