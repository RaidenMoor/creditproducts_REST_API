package com.example.creditproducts.service;

import com.example.creditproducts.model.Role;
import com.example.creditproducts.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;


    public Role getByTitle(String title) {
        Role role = roleRepository.getByTitle(title);
        return role;
    }

    public Role create(Role newRole) {
        Role existingRole = getByTitle(newRole.getTitle());
        return existingRole != null
                ? existingRole
                : roleRepository.save(newRole);
    }


    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
