package com.example.cusomer_loan.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_eligibility_rules")
@Setter
@Getter
public class LoanEligibilityRule {
    @Id
    private Integer id;
    @Column(name = "loan_type", nullable = false, unique = true)
    private String loanType;

    @Column(name = "min_cibil_score", nullable = false)
    private Integer minCibilScore;

    @Column(name = "min_income")
    private Long minIncome;

    @Column(name = "max_amount")
    private Long maxAmount;

    @Column(name = "base_interest_rate")
    private Integer baseInterestRate;


}
