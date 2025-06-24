package com.example.restbank.mapper;

import com.example.restbank.dto.UserDTO;
import com.example.restbank.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {
    public UserMapper() {
        super(User.class, UserDTO.class);
    }
}
