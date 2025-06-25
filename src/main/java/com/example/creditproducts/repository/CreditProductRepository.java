package com.example.creditproducts.repository;

import com.example.creditproducts.model.CreditProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditProductRepository extends GenericRepository<CreditProduct> {
}
