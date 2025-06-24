package com.example.restbank.repository;

import com.example.restbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    User getByUsername(String username);

}
