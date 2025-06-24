package com.example.restbank.repository;

import com.example.restbank.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends GenericRepository<Role> {

    Role getByTitle(String title);
}
