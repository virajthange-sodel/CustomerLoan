package com.example.cusomer_loan.controller;

import com.example.cusomer_loan.entities.Customer;
import com.example.cusomer_loan.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customerRepository;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.status(200).body(customerRepository.save(customer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found")));
    }
}