package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                user.getGender(),
                user.getPhone(),
                user.getPaymentCard(),
                user.getPayments(),
                user.getRdt()
        );
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findByIdAndRdtIsNull(id);
        return mapToDto(user);
    }

    @Override
    public User getByIdEntity(Long id) {
        User user = userRepository.findByIdAndRdtIsNull(id);
        return user;
    }

    @Override
    public List<UserDto> getAll() {
        List<User> userList = userRepository.findAllByRdtIsNull();
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
    public UserDto update(Long id, UserDto dto) {
        User user = getByIdEntity(id);

        if(dto.getName()!=null){
            user.setName(dto.getName());
        }
        if(dto.getSurname()!=null){
            user.setSurname(dto.getSurname());
        }
        if(dto.getEmail()!=null){
            user.setEmail(dto.getEmail());
        }
        if(dto.getPassword()!=null){
            user.setPassword(dto.getPassword());
        }
        if(dto.getPhoto()!=null){
            user.setPhoto(dto.getPhoto());
        }
        if(dto.getRole()!=null){
            user.setRole(dto.getRole());
        }
        if(dto.getGender()!=null){
            user.setGender(dto.getGender());
        }
        if(dto.getPhone()!=null){
            user.setPhone(dto.getPhone());
        }
        if(dto.getPaymentCard()!=null){
            user.setPaymentCard(dto.getPaymentCard());
        }
        if(dto.getPayments()!=null){
            user.setPayments(dto.getPayments());
        }

        return mapToDto(userRepository.save(user));
    }

    @Override
    public String deleteById(Long id) {
        User user = getByIdEntity(id);
        user.setRdt(LocalDate.now());
        userRepository.save(user);
        return "Пользователь с id: "+id+" был удален.";

    }
}
