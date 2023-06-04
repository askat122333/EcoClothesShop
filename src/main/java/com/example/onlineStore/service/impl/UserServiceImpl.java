package com.example.onlineStore.service.impl;

import com.example.onlineStore.dto.MvcDto.UserMvcDto;
import com.example.onlineStore.dto.EmailDto;
import com.example.onlineStore.dto.UserDto;
import com.example.onlineStore.entity.User;
import com.example.onlineStore.enums.Roles;
import com.example.onlineStore.exceptions.UserNotFoundException;
import com.example.onlineStore.exceptions.ValidException;
import com.example.onlineStore.repository.UserRepository;
import com.example.onlineStore.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.*;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private DefaultEmailService defaultEmailService;
    private PasswordEncoder passwordEncoder;

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole())
                .gender(user.getGender())
                .phone(user.getPhone())
                .paymentCard(user.getPaymentCard())
                .build();
    }
    private UserMvcDto mapToDtoWithImage(User user) {
        return UserMvcDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .image(user.getPhoto())
                .email(user.getEmail())
                .gender(user.getGender())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public UserDto getById(Long id) throws UserNotFoundException {
        try {
            User user = userRepository.findByIdAndRdtIsNull(id);
            return mapToDto(user);
        }catch (NullPointerException e){
            log.error("Метод getById(user), exception: Пользователь с id "+id+" не найден.");
            throw new UserNotFoundException("Пользователь с id "+id+" не найден.");
        }

    }


    @Override
    public User getByIdEntity(Long id) {
        return userRepository.findByIdAndRdtIsNull(id);
    }

    @Override
    public List<UserDto> getAll() throws UserNotFoundException {
            List<User> userList = userRepository.findAllByRdtIsNull();
            if (userList.isEmpty()) {
                log.error("Метод getAll(users) Exception: В базе нет пользователей.");
                throw new UserNotFoundException("В базе нет пользователей.");
            }
            List<UserDto> userDtoList = new ArrayList<>();
            for (User user:userList) {
                userDtoList.add(mapToDto(user));
            }
            return userDtoList;

    }

    @Override
    public UserDto create(@Valid UserDto dto) throws UserNotFoundException {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .login(dto.getLogin())
                .gender(dto.getGender())
                .paymentCard(dto.getPaymentCard())
                .role(Roles.USER)
                .build();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<User> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
            }
            log.warn("Метод create(user): "+errorMessages);
            throw new ValidException(errorMessages);
        }
        return mapToDto(userRepository.save(user));
    }


    @Override
    @Transactional
    public UserDto update(Long id,@Valid UserDto dto) throws UserNotFoundException {

            User user = getByIdEntity(id);
            if(user==null){
                throw new UserNotFoundException("Пользователь с id "+id+" не найден.");
            }

            if (dto.getName() != null) {
                user.setName(dto.getName());
            }
            if (dto.getLogin() != null) {
                user.setLogin(dto.getLogin());
            }
            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }
            if (dto.getPassword() != null) {
                user.setPassword(dto.getPassword());
            }
            if (dto.getGender() != null) {
                user.setGender(dto.getGender());
            }
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

            if (!violations.isEmpty()) {

            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<User> violation : violations) {
                errorMessages.add("Поле: " + violation.getPropertyPath() + " - " + violation.getMessage());
                log.warn("Метод update(user): "+errorMessages);
            }
            throw new ValidException(errorMessages);
        }

            return mapToDto(userRepository.save(user));

    }

    @Override
    public String deleteById(Long id) throws UserNotFoundException {
        try {
            User user = getByIdEntity(id);

            user.setRdt(LocalDate.now());
            userRepository.save(user);
            return "Пользователь с id: " + id + " был удален.";
        }catch (NullPointerException e){
            log.error("Метод deleteById(user), Exception: Пользователь с id "+id+" не найден.");
            throw new UserNotFoundException("Пользователь с id "+id+" не найден.");
        }

    }
    @Override
    public String setNewPassword(Long id,String token,String password) throws UserNotFoundException {
        try{
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
                return "Время действия токена истекло.";
        }catch (NullPointerException e){
            log.error("Метод setNewPassword(user), Exception: Пользователь с id " +id+" не найден.");
            throw new UserNotFoundException("Пользователь с id " +id+" не найден.");
        }



    }
    @Override
    @Transactional
    public String resetPassword(String email) throws UserNotFoundException {
        try {
            User user = userRepository.findByEmail(email);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            Long id = user.getId();

            user.setTokenExpiry(LocalDateTime.now());
            defaultEmailService.sendSimpleEmail(email,
                    "Link for new password.",
                    "http://localhost:9090/user/newPassword/"+id+"/"+token);
            return "Ссылка на изменение пароля была отправлена на адрес: "+email;
        }catch (NullPointerException e){
            log.error("Метод resetPassword(user), Exception: Пользователь с email "+email+" не найден.");
            throw new UserNotFoundException("Пользователь с email "+email+" не найден.");
        }

    }

    public String uploadImage(Long userId, MultipartFile file) throws UserNotFoundException {
        try {

            User user = getByIdEntity(userId);
            try {
                user.setPhoto(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(user);
            return "Image saved to database successfully!";
        }catch (NullPointerException e){
            log.error("Метод uploadImage(User), Exception: Пользователь с id "+userId+" не найден.");
            throw new UserNotFoundException("Пользователь с id "+userId+" не найден.");
        }
    }


    public  byte[] getImageById(Long id) throws UserNotFoundException {
        try {
            User user = getByIdEntity(id);
            return user.getPhoto();
        }catch (NullPointerException e){
            log.error("Метод getImageById(User), Exception: Пользователь с id "+id+" не найден.");
            throw new UserNotFoundException("Пользователь с id "+id+" не найден.");
        }

    }
    @Override
    public List<UserMvcDto> getAllMvc() throws UserNotFoundException {
        List<User> userList = userRepository.findAllByRdtIsNull();
        if (userList.isEmpty()) {
            log.error("Метод getAll(users) Exception: В базе нет пользователей.");
            throw new UserNotFoundException("В базе нет пользователей.");
        }
        List<UserMvcDto> userDtoList = new ArrayList<>();
        for (User user:userList) {
            userDtoList.add(mapToDtoWithImage(user));
        }
        return userDtoList;
    }
    @Override
    public String sendEmailToService(String email) throws IOException, UserNotFoundException {

        try {
            User user = userRepository.findByEmail(email);
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            Long id = user.getId();

            user.setTokenExpiry(LocalDateTime.now());

            EmailDto emailDto = EmailDto.builder()
                    .email(email)
                    .subject("Link for new password.")
                    .message("http://localhost:9090/user/newPassword/" + id + "/" + token)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(emailDto);

            okhttp3.MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(jsonBody, JSON);


            Request request = new Request.Builder()
                    .url("http://localhost:8080/email/model")
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();


            if (response.isSuccessful()) {

                String responseBody = response.body().string();
                System.out.println("Response: " + responseBody);
            } else {
                System.out.println("Request failed: " + response.code());
            }
            return "Ссылка на изменение пароля была отправлена на адрес: "+email;
        }catch (NullPointerException e){
            log.error("Метод resetPassword(user), Exception: Пользователь с email "+email+" не найден.");
            throw new UserNotFoundException("Пользователь с email "+email+" не найден.");
        }

    }
}
