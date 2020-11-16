package com.sales.tracker.models;

import com.sales.tracker.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Expense {

    private Long id;
    private String name;
    private String description;
    private TransactionType transactionType;
    private BigDecimal price;
}
