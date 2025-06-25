package com.example.creditproducts.repository;

import com.example.creditproducts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    User getByUsername(String username);

}
