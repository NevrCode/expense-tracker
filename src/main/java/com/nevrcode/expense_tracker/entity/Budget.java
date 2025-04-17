package com.nevrcode.expense_tracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Budgets")
public class Budget {
    @Id
    private String budget_id;

    private String budget_title;
    private String amount;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;


    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;



}
