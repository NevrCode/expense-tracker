package com.nevrcode.expense_tracker.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    private String expense_id;

    private String expense_title;

    private Date date;

    @OneToMany(mappedBy = "expense")
    private List<DetailExpense> detailExpenses;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;


}
