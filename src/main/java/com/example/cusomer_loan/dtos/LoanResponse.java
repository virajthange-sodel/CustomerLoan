package com.example.cusomer_loan.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoanResponse {
    private Integer applicationId;
    private String loanType;
    private String status;
    private Integer approvedAmount;
    private Integer interestRate;
    private Double emi;
    private Integer years;
    private String message;
    private LocalDateTime appliedAt;
}
