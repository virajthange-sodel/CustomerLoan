package com.example.cusomer_loan.repositories;

import com.example.cusomer_loan.entities.LoanEligibilityRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanEligibilityRuleRepository extends JpaRepository<LoanEligibilityRule, Integer> {

    Optional<LoanEligibilityRule> findByLoanType(String loanType);
}
