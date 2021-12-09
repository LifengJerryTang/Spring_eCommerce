package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final OrderRepository orderRepository = mock(OrderRepository.class);


    private User testUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testUser = createTestUser();
    }


    @Test
    public void submit() {
        when(userRepository.findByUsername("admin")).thenReturn(testUser);
        ResponseEntity<UserOrder> response = orderController.submit("admin");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }

    @Test
    public void getOrdersForUser() {
        when(userRepository.findByUsername("admin")).thenReturn(testUser);
        orderController.submit("admin");

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("admin");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

    }

    @Test
    public void submitWithBadUsername() {
        when(userRepository.findByUsername("not user")).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit("not user");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void getOrderForBadUser() {
        when(userRepository.findByUsername("not user")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("not user");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("password");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        cart.setId(1L);
        cart.setTotal(BigDecimal.valueOf(100.0));
        user.setCart(cart);

        return user;
    }
}
