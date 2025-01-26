package com.example.demo.config;

import jakarta.persistence.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/register", "/api/login","/h2-console/**")
                        .permitAll() // Allow permit to these urls
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // Restrict admin endpoints
                        .anyRequest().authenticated()
                )
                .headers(headers->headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user1 = User.builder()
//                .username("kiran")
//                .password("$2a$12$HxrMd2hQWnqmxj/Soyw/L.VmDY3ZK2ZbeLsXaFzj9MlnmhmX8/9wy")  //k@123
//                .roles("USER")
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("harsh")
//                .password("$2a$12$e/ZowM32kGdR1fkLPFSSZ.UDN0Ab8uQBwq3W8ForrDI.H0VD4P41m")  //h@123
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1, user2);
//    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
