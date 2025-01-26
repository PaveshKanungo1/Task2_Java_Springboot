package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    //common
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.saveNewUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
//        User loggedInUser = userService.authenticateUser(user.getEmail(), user.getPassword());
//        User loggedInUser = userService.verify(user);
          String ans = userService.verify(user);
//        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
//        return new ResponseEntity<>(null, HttpStatus.OK);
          return ans;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) throws AccessDeniedException {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    //admin-specific
    @GetMapping("/user/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() throws AccessDeniedException {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws AccessDeniedException {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully!", HttpStatus.OK);
    }

}
