package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    private final TestUtililites testUtililites = new TestUtililites();
    @InjectMocks
    private OrderController orderController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        TestUtililites.injectObjects(orderController,"userRepository", userRepository);
        TestUtililites.injectObjects(orderController,"orderRepository", orderRepository);
    }
    @Test
    public void createOrderTest(){
        testUtililites.prepareOrderTest();
        when(userRepository.findByUsername(TestUtililites.USER_NAME)).thenReturn(testUtililites.prepareOrderTest().user);

        ResponseEntity<UserOrder> response = orderController.submit(TestUtililites.USER_NAME);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        assertEquals(testUtililites.prepareOrderTest().items, order.getItems());
        assertEquals(testUtililites.prepareOrderTest().user.getUsername(), order.getUser().getUsername());
        assertEquals(testUtililites.prepareOrderTest().total, order.getTotal());
    }
    @Test
    public void viewHistoryTest(){
        TestUtililites.OrderTestData data =  testUtililites.prepareOrderTest();
        when(userRepository.findByUsername(TestUtililites.USER_NAME)).thenReturn(data.user);
        when(orderRepository.findByUser(data.user)).thenReturn(data.userOrderList);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(TestUtililites.USER_NAME);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrderListResponse = response.getBody();
        assertEquals(1, userOrderListResponse.size());
        UserOrder userOrderResponse = userOrderListResponse.get(0);
        assertEquals(data.items, userOrderResponse.getItems());
        assertEquals(data.user.getUsername(), userOrderResponse.getUser().getUsername());
        assertEquals(data.total, userOrderResponse.getTotal());
    }

}
