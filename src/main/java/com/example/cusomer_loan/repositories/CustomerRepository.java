package com.example.cusomer_loan.repositories;

import com.example.cusomer_loan.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
