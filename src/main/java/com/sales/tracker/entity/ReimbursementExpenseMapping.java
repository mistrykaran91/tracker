package com.sales.tracker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reimbursement_expense_mapping")
public class ReimbursementExpenseMapping extends BaseEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reimbursement_id")
    private ReimbursementMaster reimbursementMaster;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expense_id")
    private ExpenseMaster expenseMaster;

    @Column(name = "price")
    private BigDecimal price;
}
