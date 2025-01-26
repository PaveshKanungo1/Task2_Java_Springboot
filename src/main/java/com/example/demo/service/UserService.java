package com.example.demo.service;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //save a new user
    public User saveNewUser(User user){
        if(user.getEmail() == null || user.getPassword() == null || user.getUsername() == null || user.getRole() == null){
            throw new IllegalArgumentException("Username, email, and password cannot be null");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public String verify(User user) {
//        User user = userRepo.findByEmail(email);
//        if (user == null || !user.getPassword().equals(password)) {
//            throw new RuntimeException("Invalid email or password!");
//        }
        //may be inside UsernamePasswordAuthenticationToken, it should be email for our case
//        System.out.println("Incoming username: " + user.getUsername());
//        System.out.println("Incoming password: " + user.getPassword());

        User dbUser = userRepo.findByUsername(user.getUsername());

        if (dbUser == null) {
            throw new UserNotFoundException("User not found");
        }

        // Check password
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

//        System.out.println("Password verified");
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//        System.out.println("We are here or not");
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getUsername());
        }

//        System.out.println("Authentication Failed");
        throw new UserNotFoundException("Authentication Failed");
    }

    public List<User> getAllUsers() throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
            return userRepo.findAll();
        }

        throw new AccessDeniedException("You are not authorized to view all users");
    }

    public User getUserById(Long id) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
            return userRepo.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        }

        throw new AccessDeniedException("You are not authorized to view all users");
    }

    public User updateUser(Long id, User user){
        User existingUser = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return userRepo.save(existingUser);
    }

    public void deleteUser(Long id) throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User dbUser = userRepo.findByUsername(username);
        if(dbUser == null){
            throw new UserNotFoundException("User not found");
        }

        if("Admin".equals(dbUser.getRole())){
            userRepo.deleteById(id);
        }

        throw new AccessDeniedException("You are not authorized to view all users");
    }
}
