package com.example.restbank.service;

import com.example.restbank.model.Role;
import com.example.restbank.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private RoleRepository roleRepository;


    public Role getByTitle(String title) {
        return roleRepository.getByTitle(title);
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
