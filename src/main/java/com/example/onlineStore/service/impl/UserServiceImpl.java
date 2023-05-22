package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private DefaultEmailService defaultEmailService;
    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoto(),
                user.getRole(),
                user.getGender());
    }

    @Override
    public UserDto getById(Long id) throws UserNotFoundException {
        try {
            User user = userRepository.findByIdAndRdtIsNull(id);
            return mapToDto(user);
        }catch (NullPointerException e){
            throw new UserNotFoundException("Пользователь с id "+id+" не найден.");
        }

    }

    @Override
    public User getByIdEntity(Long id) {
        User user = userRepository.findByIdAndRdtIsNull(id);
        return user;
    }

    @Override
    public List<UserDto> getAll() throws UserNotFoundException {
        try {
            List<User> userList = userRepository.findAllByRdtIsNull();
            List<UserDto> userDtoList = new ArrayList<>();
            for (User user:userList) {
                userDtoList.add(mapToDto(user));
            }
            return userDtoList;
        }catch (NullPointerException e){
            throw new UserNotFoundException("В базе нет пользоваелей.");
        }

    }

    @Override
    public UserDto create(User user) {
        return mapToDto(userRepository.save(user));
    }


    @Override
    public UserDto update(Long id, UserDto dto) throws UserNotFoundException {

        try {
            User user = getByIdEntity(id);

            if (dto.getName() != null) {
                user.setName(dto.getName());
            }
            if (dto.getSurname() != null) {
                user.setSurname(dto.getSurname());
            }
            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null) {
                user.setPassword(dto.getPassword());
            }
            if (dto.getRole() != null) {
                user.setRole(dto.getRole());
            }
            if (dto.getGender() != null) {
                user.setGender(dto.getGender());
            }

            return mapToDto(userRepository.save(user));
        }catch (NullPointerException e){
            throw new UserNotFoundException("Пользователь с id "+id+" не найден.");
        }
    }

    @Override
    public String deleteById(Long id) {
        User user = getByIdEntity(id);
        user.setRdt(LocalDate.now());
        userRepository.save(user);
        return "Пользователь с id: "+id+" был удален.";

    }

    public String setNewPassword(Long id,String token,String password) throws UserNotFoundException {
        User user = getByIdEntity(id);
        Duration duration = Duration.ofMinutes(5);
        if (user.getToken().equals(token) &&
                Duration.between(user.getTokenExpiry(), LocalDateTime.now()).compareTo(duration)<=0)
           {
                user.setPassword(password);
                update(id, mapToDto(user));
                return user.getName()+ ": ваш пароль обновлен.";
            }
        else
            return "Неверные данные.";


    }
    public String resetPassword(String email){
        User user = userRepository.findByEmail(email);
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        Long id = user.getId();

        user.setTokenExpiry(LocalDateTime.now());
        defaultEmailService.sendSimpleEmail(email,
                "Link for new password.",
                "http://localhost:8082/user/newPassword/"+id+"/"+token);
        return "Ссылка на изменение пароля была отправлена на адрес: "+email;
    }
}
