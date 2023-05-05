package com.example.onlineStore.controller;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    @GetMapping("/user/getById/{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }
}
