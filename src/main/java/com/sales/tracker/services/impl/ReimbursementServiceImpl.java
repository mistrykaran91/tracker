package com.sales.tracker.services.impl;

import com.sales.tracker.entity.ExpenseMaster;
import com.sales.tracker.entity.ReimbursementExpenseMapping;
import com.sales.tracker.entity.ReimbursementMaster;
import com.sales.tracker.models.Expense;
import com.sales.tracker.models.Reimbursement;
import com.sales.tracker.repositories.ExpenseRepository;
import com.sales.tracker.repositories.ReimbursementExpenseMappingRepository;
import com.sales.tracker.repositories.ReimbursementRepository;
import com.sales.tracker.services.ReimbursementService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReimbursementServiceImpl implements ReimbursementService {

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ReimbursementExpenseMappingRepository reMappingRepository;

    @Autowired
    private MapperFacade mapper;

    @Override
    public List<Reimbursement> getAllReimbursement() {
        List<ReimbursementMaster> reimbursementFromDb = reimbursementRepository.findAll();
        return reimbursementFromDb.stream().map(r -> mapExpenseMasterWithAllAttributes(r, mapper.map(r, Reimbursement.class))).collect(Collectors.toList());
    }

    @Override
    public Optional<Reimbursement> getReimbursement(Long id) {
        return Optional.empty();
    }

    private Reimbursement mapExpenseMasterWithAllAttributes(ReimbursementMaster reimbursementMaster, Reimbursement reimbursement) {
        List<ReimbursementExpenseMapping> mappingList = reMappingRepository.findByReimbursementMaster(reimbursementMaster);

        if (!CollectionUtils.isEmpty(reimbursement.getExpenseList())) {
            reimbursement.setExpenseList(mapExpenseMasterFromMappingList(mappingList));
        }

        return reimbursement;
    }

    private Map<ExpenseMaster, ReimbursementExpenseMapping> getExpenseMasterMap(List<ReimbursementExpenseMapping> mappingList) {
        return mappingList.stream()
                .filter(em -> em.getExpenseMaster() != null)
                .collect(Collectors.toMap(ReimbursementExpenseMapping::getExpenseMaster, mapping -> mapping));
    }

    private List<Expense> mapExpenseMasterFromMappingList(List<ReimbursementExpenseMapping> mappingList) {

        Map<ExpenseMaster, ReimbursementExpenseMapping> expenseMasterMap = getExpenseMasterMap(mappingList);

        List<Expense> expenseList = expenseMasterMap.entrySet().stream().map(
                em -> Expense.builder()
                        .id(em.getKey().getId())
                        .name(em.getKey().getName())
                        .description(em.getKey().getDescription())
                        .transactionType(em.getKey().getTransactionType())
                        .price(em.getValue().getPrice()).build()
        ).collect(Collectors.toList());
        return expenseList;
    }

    @Override
    @Transactional
    public Reimbursement createReimbursement(Reimbursement reimbursement) {
        ReimbursementMaster reimbursementMasterDao = syncWithDB(reimbursement);
        ReimbursementMaster reimbursementMasterCreated = reimbursementRepository.save(reimbursementMasterDao);
        Reimbursement reimbursementCreated = mapper.map(reimbursementMasterCreated, Reimbursement.class);
        return updateExpenseMappingAttributes(reimbursement, reimbursementMasterCreated, reimbursementCreated);
    }

    @Override
    @Transactional
    public Reimbursement updateReimbursement(Reimbursement reimbursement) {
        ReimbursementMaster reimbursementMasterDao = syncWithDB(reimbursement);
        ReimbursementMaster reimbursementAfterUpdate = updateFields(reimbursement, reimbursementMasterDao);
        Reimbursement reimbursementCreated = mapper.map(reimbursementRepository.save(reimbursementAfterUpdate), Reimbursement.class);
        return updateExpenseMappingAttributes(reimbursement, reimbursementAfterUpdate, reimbursementCreated);
    }

    private ReimbursementMaster updateFields(Reimbursement reimbursement, ReimbursementMaster reimbursementDao) {
        if (reimbursement.getDescription() != null) {
            reimbursementDao.setDescription(reimbursement.getDescription());
        }

        if (reimbursement.getReimbursementDate() != null) {
            reimbursementDao.setReimbursementDate(reimbursement.getReimbursementDate());
        }
        return reimbursementDao;
    }

    private Map<Long, Expense> getExpenseMap(Reimbursement reimbursement) {

        if (CollectionUtils.isEmpty(reimbursement.getExpenseList())) {
            return null;
        }
        return reimbursement.getExpenseList().stream().collect(Collectors.toMap(Expense::getId, expense -> expense));
    }

    private Reimbursement updateExpenseMappingAttributes(Reimbursement reimbursement, ReimbursementMaster reimbursementMasterCreated, Reimbursement reimbursementCreated) {

        List<ReimbursementExpenseMapping> mappingList = reMappingRepository.findByReimbursementMaster(reimbursementMasterCreated);
        Map<Long, Expense> updatedExpenseMap = new HashMap<>();
        if (reimbursement.getExpenseList() != null) {
            Map<Long, Expense> expenseMap = getExpenseMap(reimbursement);
            mappingList.forEach(mapping -> {
                ReimbursementExpenseMapping reMapping = reMappingRepository.findById(mapping.getId()).get();

                if (mapping.getExpenseMaster() != null) {
                    updatedExpenseMap.put(reMapping.getExpenseMaster().getId(), saveExpenseMapping(reMapping, expenseMap));
                }
            });
            reimbursementCreated.getExpenseList().forEach(r -> {
                Expense updated = updatedExpenseMap.get(r.getId());
                r.setPrice(updated.getPrice());
            });
        }

        return reimbursementCreated;
    }

    private Expense saveExpenseMapping(ReimbursementExpenseMapping mapping, Map<Long, Expense> expenseMap) {
        if (expenseMap.containsKey(mapping.getExpenseMaster().getId())) {
            Expense expense = expenseMap.get(mapping.getExpenseMaster().getId());
            mapping.setPrice(expense.getPrice());
        }
        reMappingRepository.save(mapping);
        return mapExpenseMasterFromReMapping(mapping);
    }

    private Expense mapExpenseMasterFromReMapping(ReimbursementExpenseMapping mapping) {
        return Expense.builder().id(mapping.getExpenseMaster().getId()).price(mapping.getPrice()).build();
    }

    private ReimbursementMaster syncWithDB(Reimbursement reimbursement) {
        ReimbursementMaster reimbursementDao = getFromDB(reimbursement);

        Set<Long> associatedExpenseIds = reimbursementDao.getExpenseList().stream().map(ExpenseMaster::getId).collect(Collectors.toSet());

        if (reimbursement.getExpenseList() != null) {
            Set<Long> newAssociatedExpenseIds = reimbursement.getExpenseList().stream().map(Expense::getId).collect(Collectors.toSet());
            associatedExpenseIds.clear();
            associatedExpenseIds.addAll(newAssociatedExpenseIds);
        }

        Iterable<ExpenseMaster> associatedExpenses = expenseRepository.findAllById(associatedExpenseIds);
        List<ExpenseMaster> associatedExpensesAsList = new ArrayList<>();
        associatedExpenses.forEach(associatedExpensesAsList::add);

        reimbursementDao.setExpenseList(associatedExpensesAsList);
        return reimbursementDao;
    }

    private ReimbursementMaster getFromDB(Reimbursement reimbursement) {

        if (reimbursement.getId() != null) {
            return reimbursementRepository.findById(reimbursement.getId())
                    .orElse(mapper.map(reimbursement, ReimbursementMaster.class));
        }
        return mapper.map(reimbursement, ReimbursementMaster.class);
    }


    @Override
    public void deleteReimbursement(Long id) {
        reimbursementRepository.deleteById(id);
    }
}
