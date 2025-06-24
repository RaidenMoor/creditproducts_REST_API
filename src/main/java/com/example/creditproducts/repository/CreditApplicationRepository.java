package com.example.restbank.repository;

import com.example.restbank.model.CreditApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditApplicationRepository
        extends GenericRepository<CreditApplication> {
}
