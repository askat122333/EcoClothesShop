package com.example.onlineStore.service;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    UserDto getById(Long id);

    User getByIdEntity(Long id);

    List<UserDto> getAll();

    UserDto create(User user);

    UserDto update(Long id,UserDto dto);

    String deleteById(Long id);

}
