package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private final TestUtililites testUtililites = new TestUtililites();
    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        TestUtililites.injectObjects(userController,"userRepository", userRepository);
        TestUtililites.injectObjects(userController,"cartRepository", cartRepository);
        TestUtililites.injectObjects(userController,"bCryptPasswordEncoder", bCryptPasswordEncoder);
    }
    @Test
    public void createUserTest(){
        prepareMockForCreateUser();
        ResponseEntity<?> response = userController.createUser(TestUtililites.getCreateUserRequest());
        assertResponseForCreateUser(response);
    }

    @Test
    public void viewUserInfoTest() {
        User userTest = prepareUserTest();

        // Test find by ID
        mockFindUserById(userTest.getId(), userTest);
        ResponseEntity<User> resFindById = userController.findById(userTest.getId());
        assertUserResponse(resFindById, userTest);

        // Test find by name
        mockFindUserByUsername(TestUtililites.USER_NAME, userTest);
        ResponseEntity<User> resFindByName = userController.findByUserName(TestUtililites.USER_NAME);
        assertUserResponse(resFindByName, userTest);
    }

    private User prepareUserTest() {
        return new User();
    }

    private void mockFindUserById(Long userId, User user) {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    }

    private void mockFindUserByUsername(String username, User user) {
        when(userRepository.findByUsername(username)).thenReturn(user);
    }

    private void assertUserResponse(ResponseEntity<User> response, User expectedUser) {
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getUsername(), user.getUsername());
    }

    private void prepareMockForCreateUser() {
        when(bCryptPasswordEncoder.encode(TestUtililites.PASSWORD)).thenReturn("thisIsHashed");
    }

    private void assertResponseForCreateUser(ResponseEntity<?> response) {
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());

        User u = (User) response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(TestUtililites.USER_NAME, u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

}
