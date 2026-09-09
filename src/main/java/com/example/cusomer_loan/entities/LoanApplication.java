package com.example.cusomer_loan.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "loan_applications_table")
@Setter
@Getter
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "customer_id")
//    private Integer customerId;

    @Column(name = "loan_type", nullable = false)
    private String loanType;

    @Column(nullable = false)
    private Integer amount;

    @Column(name = "cibil_score")
    private Integer cibilScore;

    @Column(nullable = false)
    private String status;

    @Column(name = "interest_rate")
    private Integer interestRate;

    @Column(name = "emi_amount")
    private Double emi;
    private Integer years;

    @Column(name = "message")
    private String message;

    @Column(name = "applied_at")
    @CreationTimestamp
    private LocalDateTime appliedAt;

    @ManyToOne
    private Customer customer;
}