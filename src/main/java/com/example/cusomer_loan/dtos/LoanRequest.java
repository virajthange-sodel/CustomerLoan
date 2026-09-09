package com.example.cusomer_loan.dtos;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class LoanRequest {
    private Integer customerId;
    private String loanType;
    private Integer cibilScore;
    private Integer loanAmount;
    private Integer years;
    private String action;
}
