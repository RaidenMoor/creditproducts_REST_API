package com.example.creditproducts.repository;

import com.example.creditproducts.model.CreditApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditApplicationRepository
        extends GenericRepository<CreditApplication> {


    Optional<CreditApplication> findByClientId(Long id);
}
