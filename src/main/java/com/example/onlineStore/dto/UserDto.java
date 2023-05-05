package com.example.onlineStore.dto;

import com.example.onlineStore.enums.Gender;
import com.example.onlineStore.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Byte[] photo;
    private Roles role;
    private Gender gender;
}
