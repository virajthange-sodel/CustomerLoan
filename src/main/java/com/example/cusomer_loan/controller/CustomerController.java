package com.example.cusomer_loan.controller;

import com.example.cusomer_loan.entities.Acc;
import com.example.cusomer_loan.entities.Customer;
import com.example.cusomer_loan.repositories.AccRepository;
import com.example.cusomer_loan.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AccRepository accRepository;

    private static int trans1Attempt = 1;
    private static int trans2Attempt = 1;
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        if(customer.getPan().length() != 10) throw new RuntimeException("Length of pan card number should be 10");

        Customer save = customerRepository.save(customer);
        kafkaTemplate.send("user-reg", "User created with id: "+ save.getId());
        return ResponseEntity.status(200).body(save);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found")));
    }


    @GetMapping("/trans1")
    @Transactional
    @Retryable(
//            includes = DeadlockLoserDataAccessException.class,
            maxRetries = 3
    )
    public void trnas1() {
        System.out.println("Transaction1 method is called");
        System.out.println("Transaction 1 attempt is: "+ trans1Attempt++);

        Acc acc1 = accRepository.findAccountForUpdate(1).orElseThrow(() -> new RuntimeException("Account not found"));
        try {
            System.out.println("Transaction 1 is running");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        acc1.setAmount(acc1.getAmount() - 100);

        Acc acc2 = accRepository.findAccountForUpdate(2).orElseThrow(() -> new RuntimeException("Account not found"));

        acc2.setAmount(acc2.getAmount() - 100);

        accRepository.save(acc1);
        accRepository.save(acc2);
        System.out.println("Transaction 1 finished");
    }


    @GetMapping("/trans2")
    @Transactional
    @Retryable(
//            includes = DeadlockLoserDataAccessException.class,
            maxRetries = 3
    )
    public void trnas2() {

        System.out.println("Transaction2 method is called");

        System.out.println("Transaction 2 attempt is: "+ trans2Attempt++);
        Acc acc2 = accRepository.findAccountForUpdate(2).orElseThrow(() -> new RuntimeException("Account not found"));

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        acc2.setAmount(acc2.getAmount() - 100);

        Acc acc1 = accRepository.findAccountForUpdate(1).orElseThrow(() -> new RuntimeException("Account not found"));
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        acc1.setAmount(acc1.getAmount() - 100);

        accRepository.save(acc1);
        accRepository.save(acc2);
        System.out.println("Transaction 2 finished");
    }
}