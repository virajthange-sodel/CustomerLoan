package com.example.cusomer_loan.controller;

import com.example.cusomer_loan.dtos.ActionLoanRequest;
import com.example.cusomer_loan.dtos.LoanRequest;
import com.example.cusomer_loan.dtos.LoanResponse;
import com.example.cusomer_loan.entities.LoanApplication;
import com.example.cusomer_loan.service.LoanService;
import com.example.cusomer_loan.utilities.Encrypted;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loan")
public class LoanController {
    private final LoanService loanService;

    @GetMapping("/act")
    public ResponseEntity<?> performAction(@RequestBody ActionLoanRequest actionLoanRequest) {
        switch(actionLoanRequest.getAction()) {
            case "POST": {
                LoanRequest loanRequest = new LoanRequest();
                BeanUtils.copyProperties(actionLoanRequest, loanRequest );
                System.out.println("In post switch" + loanRequest);
                return ResponseEntity.status(200).body(loanService.processApplication(loanRequest));
            }
            case "GET" : {
                LoanResponse applicationById = loanService.getApplicationById(actionLoanRequest.getApplicationId());
                return ResponseEntity.status(200).body(applicationById);
            }
            case "DELETE" : {
                loanService.deleteById(actionLoanRequest.getApplicationId());
                return ResponseEntity.status(200).body("Application deleted successfully");
            }
        }
        return ResponseEntity.status(300).body("Invalid action");
    }

    @PostMapping("/apply")
    @Encrypted
    public ResponseEntity<LoanResponse> applyLoan(@RequestBody LoanRequest request) {
        System.out.println("request coming :"+ request);
        System.out.println("Loan type is: "+request.getLoanType());
        LoanResponse response = loanService.processApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<LoanResponse> getLoanApplication(@PathVariable Integer applicationId) {
        LoanResponse response = loanService.getApplicationById(applicationId);
        System.out.println(response);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanApplication>> getLoansByCustomer(@PathVariable Long customerId) {
        List<LoanApplication> responses = loanService.getApplicationsByCustomer(customerId);
        return ResponseEntity.ok(responses);
    }
}