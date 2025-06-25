package com.example.creditproducts.repository;

import com.example.creditproducts.model.IssuedLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssuedLoanRepository extends GenericRepository<IssuedLoan> {
}
