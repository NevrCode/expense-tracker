package com.nevrcode.expense_tracker.entity;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detail_expenses")
public class DetailExpense {
    @Id
    private String detail_id;

    @OneToOne
    @JoinColumn(name = "expense_id", referencedColumnName = "expense_id")
    private Expense expense;

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;
    @Nullable
    private int Quantity;
    
}
