package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestUtililites {

    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin1234";

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        Field field = null;
        boolean wasAccessible = false;
        try {
            field = target.getClass().getDeclaredField(fieldName);
            wasAccessible = field.isAccessible();
            field.setAccessible(true);
            field.set(target, toInject);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace(); // Replace with a logging framework in real applications
        } finally {
            if (field != null) {
                field.setAccessible(wasAccessible);
            }
        }
    }

    public static ModifyCartRequest createModifyCartRequest(String username, Long itemId, int quantity) {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
    }
    public List<Item> prepareTestItems() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round"));
        items.add(new Item(2L, "Square Widget", BigDecimal.valueOf(1.99), "A widget that is square"));
        return items;
    }
    public void assertResponseItemById(ResponseEntity<Item> response, Item expectedItem) {
        assertNotNull(response, "The response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "The status code should be 200");
        assertEquals(expectedItem, response.getBody(), "The response body should match the expected item");
    }

    public void assertResponseItemsByName(ResponseEntity<List<Item>> response, List<Item> expectedItems) {
        assertNotNull(response, "The response should not be null");
        assertEquals(200, response.getStatusCodeValue(), "The status code should be 200");
        assertEquals(expectedItems, response.getBody(), "The response body should match the expected items");
    }
    public OrderTestData prepareOrderTest(){
        Cart cart = new Cart();
        User userTest = new User(1L, TestUtililites.USER_NAME, TestUtililites.PASSWORD, cart);
        List<Item> items = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        Item item1 = new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round");
        Item item2 = new Item(2L, "Square Widget", BigDecimal.valueOf(1.99), "A widget that is square");
        items.add(item1);
        items.add(item2);
        total.add(item1.getPrice());
        total.add(item2.getPrice());
        cart.setItems(items);
        cart.setTotal(total);
        cart.setUser(userTest);
        UserOrder userOrder = UserOrder.createFromCart(cart);
        List<UserOrder> userOrderList = Collections.singletonList(userOrder);
        return new OrderTestData(userTest, items, total,userOrderList);
    }
    public static class OrderTestData {
        User user;
        List<Item> items;
        BigDecimal total;
        List<UserOrder> userOrderList;

        OrderTestData(User user, List<Item> items, BigDecimal total, List<UserOrder> userOrderList) {
            this.user = user;
            this.items = items;
            this.total = total;
            this.userOrderList = userOrderList;
        }
    }
    public static CreateUserRequest getCreateUserRequest() {
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername(TestUtililites.USER_NAME);
        r.setPassword(TestUtililites.PASSWORD);
        r.setRepeatPassword(TestUtililites.PASSWORD);
        return r;
    }

}
