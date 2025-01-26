package com.example.demo.service;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private Authentication authentication;

    private User existingUser;
    private User updatedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        if (authentication == null) {
            authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn("admin");
            SecurityContextHolder.setContext(mock(SecurityContext.class));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        existingUser = new User("existingUser", "existing@email.com", "existingPass", "User");
        updatedUser = new User("updatedUser", "updated@email.com", "updatedPass", "User");
    }

    @Test
    public void testAdd(){
        assertEquals(4, 2+2);
    }


//    @ParameterizedTest
//    @ArgumentsSource(UserArgumentsProvider.class)
//    public void testSaveNewUser(User user){
//
//        User savedUser = userService.saveNewUser(user);
//
//        assertNotNull(savedUser, "The saved user should not be null.");
//        assertNotNull(savedUser.getId(), "The saved user's ID should not be null.");
//        assertTrue(savedUser.getPassword().startsWith("$2a$"), "The password should be encoded using BCrypt.");
//        assertEquals(user.getUsername(), savedUser.getUsername(), "The username should match the input user.");
//        assertEquals(user.getEmail(), savedUser.getEmail(), "The email should match the input user.");
//        assertEquals(user.getRole(), savedUser.getRole(), "The role should match the input user.");
//    }

    @ParameterizedTest
    @ArgumentsSource(UserArgumentsProvider.class)
    public void testSaveNewUser(User user){

        //mocking the behaviour of userRepo and encoder

        when(encoder.encode(user.getPassword())).thenReturn("$2a$MockEncodedPassword");

        when(userRepo.save(Mockito.any(User.class))).thenAnswer(invocation -> {
           User savedUser = (User)invocation.getArgument(0);
           savedUser.setId(1L);
           return savedUser;
        });

        User savedUser = userService.saveNewUser(user);

        assertNotNull(savedUser, "The saved user should not be null.");
        assertNotNull(savedUser.getId(), "The saved user's ID should not be null.");
        assertEquals("$2a$MockEncodedPassword", savedUser.getPassword(), "The password should be encoded.");
        assertEquals(user.getUsername(), savedUser.getUsername(), "The username should match the input user.");
        assertEquals(user.getEmail(), savedUser.getEmail(), "The email should match the input user.");
        assertEquals(user.getRole(), savedUser.getRole(), "The role should match the input user.");
    }

    @Test
    public void testGetAllUsers_Admin() throws AccessDeniedException {
        User adminUser = new User("admin", "admin@example.com", "password", "Admin");
        User normalUser = new User("user1", "user1@example.com", "password", "User");

        when(userRepo.findByUsername("admin")).thenReturn(adminUser);
        when(userRepo.findAll()).thenReturn(List.of(adminUser, normalUser));

        List<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size(), "The list should contain two users.");
    }

    @Test
    public void testUpdateUser_Success() {
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepo.save(Mockito.any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername(), "The username should be updated.");
        assertEquals("updated@email.com", result.getEmail(), "The email should be updated.");
        assertEquals("updatedPass", result.getPassword(), "The password should be updated.");
        verify(userRepo, times(1)).save(Mockito.any(User.class)); // Ensure save was called once
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepo.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, updatedUser),
                "UserNotFoundException should be thrown when the user is not found.");
    }

}
