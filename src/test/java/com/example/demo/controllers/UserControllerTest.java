package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    private User testUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUser = createTestUser();
    }

    @Test
    public void createUser() {
        when(bCryptPasswordEncoder.encode("password")).thenReturn("hashedPassword");

        CreateUserRequest testCreateUserRequest = createTestCreateUserRequest();
        ResponseEntity<User> response = userController.createUser(testCreateUserRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("admin", testCreateUserRequest.getUsername());
        assertEquals(0, response.getBody().getId());
        assertEquals("hashedPassword", response.getBody().getPassword());
    }

    @Test
    public void findByUsername() {
        when(userRepository.findByUsername("admin")).thenReturn(testUser);
        ResponseEntity<User> user = userController.findByUserName("admin");
        assertEquals(200, user.getStatusCodeValue());
    }

    @Test
    public void findById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        ResponseEntity<User> user = userController.findById(1L);
        assertEquals(200, user.getStatusCodeValue());
    }

    private CreateUserRequest createTestCreateUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("admin");
        createUserRequest.setPassword("password");
        createUserRequest.setConfirmPassword("password");

        return createUserRequest;
    }

    @Test
    public void getUserByBadId() {
        when(userRepository.findById(11L)).thenReturn(Optional.ofNullable(null));
        ResponseEntity<User> user = userController.findById(11L);
        assertEquals(404, user.getStatusCodeValue());
    }

    @Test
    public void getUserByBadUsername() {
        when(userRepository.findByUsername("not user")).thenReturn(null);
        ResponseEntity<User> user = userController.findByUserName("not user");
        assertEquals(404, user.getStatusCodeValue());
    }


    public User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("password");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        user.setCart(cart);

        return user;
    }
}
