package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private final UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Mock
    private final CartRepository cartRepository = mock(CartRepository.class);

    private User user;

    private Cart cart;

    private Item item;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        user = createTestUser();
        item = createTestItem();
        cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
    }

    @Test
    public void addToCart() {

        when(userRepository.findByUsername("admin")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createTestModifyCartRequest("admin", 1L, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cartResponse = responseEntity.getBody();
        assertNotNull(cartResponse);

        List<Item> items = cartResponse.getItems();
        assertNotNull(items);

        assertEquals("admin", cartResponse.getUser().getUsername());


    }

    @Test
    public void removeFromCart() {

        when(userRepository.findByUsername("admin")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ModifyCartRequest modifyCartRequest = createTestModifyCartRequest("admin", 1L, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart cartResponse = responseEntity.getBody();
        assertNotNull(cartResponse);

        List<Item> items = cartResponse.getItems();
        List<Item> removedItems = items.stream().filter(item -> item.getId().equals(1L)).collect(Collectors.toList());
        assertEquals(0, removedItems.size());

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

    public Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("An item");
        item.setDescription("Test item");
        item.setPrice(BigDecimal.valueOf(1.0));

        return item;
    }

    @Test
    public void addToCartBadUser(){

        ModifyCartRequest modifyCartRequest = createTestModifyCartRequest("not user", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartNoBadItem(){

        when(userRepository.findByUsername("admin")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        ModifyCartRequest modifyCartRequest = createTestModifyCartRequest("admin", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartBadUser(){

        ModifyCartRequest modifyCartRequest = createTestModifyCartRequest("not user", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromCartBadItem(){

        when(userRepository.findByUsername("admin")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(null));

        ModifyCartRequest modifyCartRequest = createTestModifyCartRequest("admin", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    public ModifyCartRequest createTestModifyCartRequest(String username, long itemId, int quantity) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);

        return modifyCartRequest;
    }
}
