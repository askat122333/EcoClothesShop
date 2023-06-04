package com.example.onlineStore.controller;

import com.example.onlineStore.dto.MvcDto.UserMvcDto;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable @Min(1) Long id) throws UserNotFoundException {
        return userService.getById(id);
    }
    @GetMapping("/all")
    public List<UserDto> getAll() throws UserNotFoundException {
        return userService.getAll();
    }

    @GetMapping("/allWithImage")
    public List<UserMvcDto> getAllWithImage() throws UserNotFoundException {
        return userService.getAllMvc();
    }
    @PostMapping("/create")
    public UserDto addNewUser(@RequestBody UserDto dto) throws UserNotFoundException {
        return userService.create(dto);
    }
    @PutMapping("/update/{id}")
    public UserDto updateUser(@PathVariable Long id,
                              @RequestBody UserDto dto) throws UserNotFoundException {
        return userService.update(id,dto);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) throws UserNotFoundException {
        return userService.deleteById(id);
    }

    @PutMapping("/newPassword/{id}/{token}")
    public String newPassword(@PathVariable Long id,
                              @PathVariable String token,
                              @RequestParam("password") String password) throws UserNotFoundException {
        return userService.setNewPassword(id,token,password);
    }
    @PutMapping("/resetPassword")
    public String resetPassword(@RequestParam("email") String email) throws UserNotFoundException {
        return userService.resetPassword(email);
    }
}
