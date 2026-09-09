package com.example.cusomer_loan.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "customer_table")
@Setter
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstname;
    private String lastname;
    @Column(name = "pan_number", unique = true)
    private String pan;
    @Column(name = "cibil_score")
    private Integer cibilscore;
    private Integer income;

    @OneToMany(mappedBy = "customer")
    private List<LoanApplication> loanApplications;

}
