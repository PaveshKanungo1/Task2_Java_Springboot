package com.example.demo.service;


import com.example.demo.model.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(new User("Ram", "ram@email.com", "Ram123", "User")),
                Arguments.of(new User("Shyam", "shyam@email.com", "Shyam123", "Admin"))
        );
    }
}
