package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    private TestUtililites testUtililites = new TestUtililites();
    @InjectMocks
    private ItemController itemController;
    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtililites.injectObjects(itemController,"itemRepository", itemRepository);
    }
    @Test
    public void getAllItemsTest() {
        // Prepare test data
        List<Item> expectedItems = testUtililites.prepareTestItems();
        when(itemRepository.findAll()).thenReturn(expectedItems);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response, "The response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "The status code should be 200");
        assertEquals(expectedItems, response.getBody(), "The response body should match the expected items");
    }
    @Test
    public void getItemByTest() {
        // Prepare test data
        List<Item> items = testUtililites.prepareTestItems();
        Item item1 = items.get(0);
        Item item2 = items.get(1);

        // Test find by ID
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        ResponseEntity<Item> responseID = itemController.getItemById(2L);
        testUtililites.assertResponseItemById(responseID, item2);

        // Test find by name
        when(itemRepository.findByName("Round Widget")).thenReturn(Collections.singletonList(item1));
        ResponseEntity<List<Item>> responseByName = itemController.getItemsByName("Round Widget");
        testUtililites.assertResponseItemsByName(responseByName, Collections.singletonList(item1));
    }
}
