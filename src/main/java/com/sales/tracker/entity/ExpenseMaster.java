package com.sales.tracker.entity;

import com.sales.tracker.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expense_master")
public class ExpenseMaster extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}