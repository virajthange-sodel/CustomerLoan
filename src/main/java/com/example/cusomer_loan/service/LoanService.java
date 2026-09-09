package com.example.cusomer_loan.service;

import com.example.cusomer_loan.dtos.LoanRequest;
import com.example.cusomer_loan.dtos.LoanResponse;
import com.example.cusomer_loan.entities.Customer;
import com.example.cusomer_loan.entities.LoanApplication;
import com.example.cusomer_loan.entities.LoanEligibilityRule;
import com.example.cusomer_loan.repositories.CustomerRepository;
import com.example.cusomer_loan.repositories.LoanApplicationRepository;
import com.example.cusomer_loan.repositories.LoanEligibilityRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final CustomerRepository customerRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanEligibilityRuleRepository loanEligibilityRuleRepository;

    public LoanResponse processApplication(LoanRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new RuntimeException("User not found."));

        LoanEligibilityRule byLoanType = loanEligibilityRuleRepository.findByLoanType(request.getLoanType()).orElseThrow(() -> new RuntimeException("Loan type doesn't exist..."));

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setAmount(request.getLoanAmount());
        loanApplication.setCibilScore(request.getCibilScore());
//        loanApplication.setCustomerId(request.getCustomerId());
        loanApplication.setAppliedAt(LocalDateTime.now());
        loanApplication.setLoanType(request.getLoanType());
        loanApplication.setCustomer(customer);

        if (request.getCibilScore() < byLoanType.getMinCibilScore()) {
            loanApplication.setStatus("REJECTED");
            loanApplication.setMessage("Cibil score is low");
            LoanApplication saved = loanApplicationRepository.save(loanApplication);
//            throw new RuntimeException("Cibil score is low, can't proceed further");
            LoanResponse loanResponse = new LoanResponse();
            loanResponse.setApplicationId(saved.getId());
            loanResponse.setAppliedAt(saved.getAppliedAt());
            loanResponse.setStatus("REJECTED");
            loanResponse.setMessage("Loan application rejected");
            loanResponse.setLoanType(request.getLoanType());
            return loanResponse;
        }

        loanApplication.setStatus("APPROVED");
        loanApplication.setMessage("Application accepted");
        loanApplication.setEmi(calculateEmi(request.getLoanAmount(), request.getYears() , byLoanType.getBaseInterestRate()));
//        loanApplicatio
        loanApplication.setYears(request.getYears());
        loanApplication.setInterestRate(byLoanType.getBaseInterestRate());
        LoanApplication saved = loanApplicationRepository.save(loanApplication);

        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setApplicationId(saved.getId());
        loanResponse.setAppliedAt(saved.getAppliedAt());
        loanResponse.setInterestRate(saved.getInterestRate());
        loanResponse.setMessage("Loan application approved successfully");
        loanResponse.setApprovedAmount(saved.getAmount());
        loanResponse.setStatus("APPROVED");
        loanResponse.setLoanType(saved.getLoanType());
        loanResponse.setYears(loanApplication.getYears());
        loanResponse.setEmi(loanApplication.getEmi());

        return loanResponse;
    }

    private Double calculateEmi(Integer loanAmount, Integer years, Integer baseInterestRate) {
        double principal = loanAmount;

        double monthlyRate = baseInterestRate / 12.0 / 100.0;

        int months = years * 12;

        double emi = (principal * monthlyRate * Math.pow(1 + monthlyRate, months))
                / (Math.pow(1 + monthlyRate, months) - 1);
//        return Math.round(emi);
        return emi;
    }

    public LoanResponse getApplicationById(Integer applicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(applicationId).orElseThrow(() -> new RuntimeException("Application not found."));
        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setApplicationId(loanApplication.getId());
        loanResponse.setStatus(loanApplication.getStatus());
        loanResponse.setLoanType(loanApplication.getLoanType());
        loanResponse.setAppliedAt(loanApplication.getAppliedAt());
        loanResponse.setApprovedAmount(loanApplication.getAmount());
        loanResponse.setMessage(loanApplication.getMessage());
        loanResponse.setYears(loanApplication.getYears());
        loanResponse.setEmi(loanApplication.getEmi());
        return loanResponse;
    }

    public List<LoanApplication> getApplicationsByCustomer(Long customerId) {
        List<LoanApplication> byCustomerId = loanApplicationRepository.findByCustomerId(customerId);
        return byCustomerId;
    }

    public void deleteById(Integer applicationId) {
        loanApplicationRepository.deleteById(applicationId);
    }
}