package com.example.creditproducts.repository;

import com.example.creditproducts.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends GenericRepository<Role> {

    Role getByTitle(String title);
}
