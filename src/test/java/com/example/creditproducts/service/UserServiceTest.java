package com.example.creditproducts.service;

import com.example.creditproducts.dto.UserDTO;
import com.example.creditproducts.mapper.UserMapper;
import com.example.creditproducts.model.Role;
import com.example.creditproducts.model.User;
import com.example.creditproducts.repository.UserRepository;
import com.example.creditproducts.service.RoleService;
import com.example.creditproducts.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.creditproducts.constant.RoleConstants.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User userEntity;
    private Role userRole;


    @BeforeEach
    void setUp() {
        // Инициализация объектов для тестов
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("password");

        userEntity = new User();
        userEntity.setUsername("testUser");
        userEntity.setPassword("password");

        userRole = new Role(USER, "Пользователь");
    }

    @Test
    void getByUsername_ExistingUsername_ReturnsUserDTO() {
        // Arrange
        when(userRepository.getByUsername("testUser")).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.getByUsername("testUser");

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).getByUsername("testUser");
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void getByUsername_NonExistingUsername_ReturnsNull() {
        // Arrange
        when(userRepository.getByUsername("nonExistingUser")).thenReturn(null);

        // Act
        UserDTO result = userService.getByUsername("nonExistingUser");

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).getByUsername("nonExistingUser");
        verify(userMapper, never()).toDTO(any()); // Подтверждаем, что toDTO никогда не вызывался
    }

    @Test
    void create_NewUser_ReturnsCreatedUserDTO() {
        // Arrange
        when(roleService.getByTitle(USER)).thenReturn(userRole);
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.create(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertNotNull(result.getRole());
        assertEquals(USER, result.getRole().getTitle());

        verify(roleService, times(1)).getByTitle(USER);
        verify(userMapper, times(1)).toEntity(any(UserDTO.class));
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDTO(userEntity);
    }

    @Test
    void create_RoleDoesNotExist_CreatesNewRole() {
        // Arrange
        when(roleService.getByTitle(USER)).thenReturn(null);
        when(roleService.create(any(Role.class))).thenReturn(userRole);
        when(userMapper.toEntity(any(UserDTO.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.create(userDTO);

        // Assert
        assertNotNull(result);
        verify(roleService, times(1)).getByTitle(USER);
        verify(roleService, times(1)).create(any(Role.class));
        verify(userMapper, times(1)).toEntity(any(UserDTO.class));
        verify(userRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toDTO(userEntity);
    }
}