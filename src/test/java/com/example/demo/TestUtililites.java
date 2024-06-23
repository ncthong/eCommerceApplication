package com.example.demo;

import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;

public class TestUtililites {

    public static final String USER_NAME = "admin";
    public static final String PASSWORD = "admin";

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
}
