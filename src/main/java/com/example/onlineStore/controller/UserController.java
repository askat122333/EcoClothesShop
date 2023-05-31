package com.example.onlineStore.controller;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("productId") Long productId,
                              @RequestParam("imageFile") MultipartFile file) throws UserNotFoundException {
        return userService.uploadImage(productId, file);
    }


    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageById(@PathVariable("id") Long id) throws Exception {
        return userService.getImageById(id);
    }
}
