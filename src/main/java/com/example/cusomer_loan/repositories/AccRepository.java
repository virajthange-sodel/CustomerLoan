package com.example.cusomer_loan.repositories;

import com.example.cusomer_loan.entities.Acc;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccRepository extends JpaRepository<Acc, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)                         //It allows to lock the rows
    @Query("SELECT a FROM Acc a WHERE a.id = :id")
    Optional<Acc> findAccountForUpdate(@Param("id") Integer id);
}
