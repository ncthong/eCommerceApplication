package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtililites.injectObjects(cartController,"userRepository", userRepository);
        TestUtililites.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtililites.injectObjects(cartController,"itemRepository", itemRepository);
    }

    @Test
    public void addToCartTest(){
        Cart cart = new Cart();
        User userTest = new User(1L, TestUtililites.USER_NAME, TestUtililites.PASSWORD, cart);
        Item item1 = new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round");
        ModifyCartRequest modifyCartRequest = TestUtililites.createModifyCartRequest(TestUtililites.USER_NAME, 1L, 2);
        when(userRepository.findByUsername(TestUtililites.USER_NAME)).thenReturn(userTest);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cartAdd = responseEntity.getBody();
        assertNotNull(cartAdd);
        assertEquals(2, cartAdd.getItems().size());
        assertEquals(item1, cartAdd.getItems().get(0));
        assertEquals(item1, cartAdd.getItems().get(1));
    }
    @Test
    public void removeFromCartTest(){
        Cart cart = new Cart();
        User userTest = new User(1L, TestUtililites.USER_NAME, TestUtililites.PASSWORD, cart);
        Item item1 = new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round");
        // Add item to cart first
        cart.addItem(item1);
        when(userRepository.findByUsername(TestUtililites.USER_NAME)).thenReturn(userTest);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        ModifyCartRequest modifyCartRequest = TestUtililites.createModifyCartRequest(TestUtililites.USER_NAME, 1L, 1);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cartRemove = responseEntity.getBody();
        assertNotNull(cartRemove);
        assertEquals(cartRemove.getItems(), new ArrayList<>());
    }
}
