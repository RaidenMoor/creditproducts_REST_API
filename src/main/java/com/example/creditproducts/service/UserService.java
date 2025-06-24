package com.example.restbank.service;

import com.example.restbank.dto.UserDTO;
import com.example.restbank.mapper.UserMapper;
import com.example.restbank.model.Role;
import com.example.restbank.model.User;
import com.example.restbank.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.example.restbank.constant.RoleConstants.USER;

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
