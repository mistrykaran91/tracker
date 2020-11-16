package com.sales.tracker.services;

import com.sales.tracker.models.Reimbursement;

import java.util.List;
import java.util.Optional;

public interface ReimbursementService {

    List<Reimbursement> getAllReimbursement();

    Optional<Reimbursement> getReimbursement(Long id);

    Reimbursement createReimbursement(Reimbursement reimbursement);

    Reimbursement updateReimbursement(Reimbursement reimbursement);

    void deleteReimbursement(Long id);

}
