package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoto(),
                user.getRole(),
                user.getGender()
        );
    }

    @Override
    public UserDto getById(Long id) {
        Optional<User> user = Optional.of(userRepository.findById(id).orElse(new User()));
        return mapToDto(user.get());
    }

    @Override
    public User getByIdEntity(Long id) {
        User user = userRepository.findById(id).get();
        return user;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user:userList) {
            userDtoList.add(mapToDto(user));
        }
        return userDtoList;
    }

    @Override
    public UserDto create(User user) {
        return mapToDto(userRepository.save(user));
    }

    @Override
    public UserDto update(User user) {
        return mapToDto(userRepository.save(user));
    }

    @Override
    public String deleteById(Long id) {
        return null;
    }
}
