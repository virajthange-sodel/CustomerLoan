package com.example.cusomer_loan.repositories;

import com.example.cusomer_loan.dtos.LoanResponse;
import com.example.cusomer_loan.entities.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication,Integer> {
    public Optional<LoanApplication> findByLoanType(String loanType);

    List<LoanApplication> findByCustomerId(Long customerId);
}
