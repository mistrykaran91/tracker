package com.sales.tracker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "reimbursement_master")
public class ReimbursementMaster extends BaseEntity {

    @Column(name = "reimbursement_date", nullable = false)
    private Date reimbursementDate;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reimbursement_expense_mapping",
            joinColumns = @JoinColumn(name = "reimbursement_id"),
            inverseJoinColumns = @JoinColumn(name = "expense_id"))
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    private List<ExpenseMaster> expenseList;
}
