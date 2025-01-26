package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.UserPrincipal;
import com.example.demo.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username you entered is: "+ username);
        User user = userRepo.findByUsername(username);
        if(user == null) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("user not found");
        }

        return new UserPrincipal(user);
    }
}
