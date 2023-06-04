package com.example.onlineStore.service;

import com.example.onlineStore.dto.MvcDto.UserMvcDto;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface UserService {
    UserDto getById(Long id) throws UserNotFoundException;

    User getByIdEntity(Long id);

    List<UserDto> getAll() throws UserNotFoundException;

    UserDto create(UserDto dto) throws UserNotFoundException;

    UserDto update(Long id,UserDto dto) throws UserNotFoundException;

    String deleteById(Long id) throws UserNotFoundException;
    String setNewPassword(Long id,String token, String password) throws UserNotFoundException;
    String resetPassword(String email) throws UserNotFoundException;
    String uploadImage(Long userId, MultipartFile file) throws UserNotFoundException;
    byte[] getImageById(Long id) throws UserNotFoundException;
    String sendEmailToService(String email) throws IOException, UserNotFoundException;
    List<UserMvcDto> getAllMvc() throws UserNotFoundException;
    com.example.onlineStore.dto.MvcDto.UserMvcDto getByIdWithPhoto(Long id) throws UserNotFoundException;

}
