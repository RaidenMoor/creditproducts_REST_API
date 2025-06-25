package com.example.creditproducts.mapper;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {
    public UserMapper() {
        super(User.class, UserDTO.class);
    }
}
