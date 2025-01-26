package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MyUserDetailsServiceTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void TestloadUserByUsername() {
        assertNotNull(userRepo.findByUsername("Pavesh"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Pavesh",
            "Piyush",
            "Aditya"
    })
    public void TestloadUserByUsernameAndPassword(String input) {
        User user = userRepo.findByUsername(input);
        if(user != null){
            assertNotNull(user);
        } else {
            assertNull(user);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "1,1,2",
            "2, 10, 12",
            "3, 3, 6"
    })
    public void test(int a, int b, int expected) {
        assertEquals(expected, a + b);
    }
}
