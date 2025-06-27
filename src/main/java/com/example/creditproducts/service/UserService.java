package com.example.creditproducts.service;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.mapper.UserMapper;
import com.example.creditproducts.model.Role;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.creditproducts.constant.RoleConstants.USER;

@Service
public class UserService extends GenericService<User, UserDTO> {
    public UserService(UserRepository userRepository, UserMapper userMapper){
       repository= userRepository;
        mapper = userMapper;
    }

    @Autowired
    private RoleService roleService;


    public UserDTO getByUsername(String login) {
        return mapper.toDTO(((UserRepository) repository).getByUsername(login));
    }

    @Override
    public UserDTO create(UserDTO userDTO){
        UserDTO newUser = new UserDTO();
        Role userRole = roleService.getByTitle(USER);
        if (userRole == null) {
            userRole = roleService.create(new Role(USER, "Пользователь"));
        }
        newUser.setRole(userRole);
        newUser.setPassword(userDTO.getPassword());
        newUser.setUsername(userDTO.getUsername());
        User entity = mapper.toEntity(newUser);
        return mapper.toDTO(repository.save(entity));
    }






}
