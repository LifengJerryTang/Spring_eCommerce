package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private Item testItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testItem = new Item();
        testItem.setId(1L);
        testItem.setName("Item 1");
    }

    @Test
    public void getItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(testItem));
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(testItem);
        when(itemRepository.findByName("Item 1")).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void getItemByBadId() {
        when(itemRepository.findById(30L)).thenReturn(Optional.ofNullable(null));
        ResponseEntity<Item> response = itemController.getItemById(30L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void getItemByBadName() {
        List<Item> itemList = new ArrayList<>();
        when(itemRepository.findByName("No Name")).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName("No Name");
        assertNotNull(response);

        assertEquals(404, response.getStatusCodeValue());
    }
}
