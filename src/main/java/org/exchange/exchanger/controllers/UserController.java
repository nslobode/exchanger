package org.exchange.exchanger.controllers;

import lombok.RequiredArgsConstructor;
import org.exchange.exchanger.dto.UserDto;
import org.exchange.exchanger.entities.User;
import org.exchange.exchanger.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/users/{passport}", produces = "application/x-yaml")
    public User findUserById(@PathVariable("passport") Integer passport) {
        return userService.getUserByPassport(passport);
    }

    @PostMapping(value = "/users/save", produces = "application/x-yaml")
    public UserDto saveUser(UserDto userDto) {
       return userService.saveUser(userDto);
    }
}
