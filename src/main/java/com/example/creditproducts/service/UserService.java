package com.example.creditproducts.service;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.mapper.UserMapper;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericService<User, UserDTO> {
    public UserService(UserRepository userRepository, UserMapper userMapper){
       repository= userRepository;
        mapper = userMapper;
    }

    private RoleService roleService;


    public UserDTO getByUsername(String login) {
        return mapper.toDTO(((UserRepository) repository).getByUsername(login));
    }






}
