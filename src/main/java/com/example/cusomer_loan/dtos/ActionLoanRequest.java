package com.example.cusomer_loan.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionLoanRequest {
    private Integer customerId;
    private String loanType;
    private Integer cibilScore;
    private Integer loanAmount;
    private Integer years;
    private String action;
    private Integer applicationId;
}
