package com.example.onlineStore.dto.MvcDto;

import com.example.onlineStore.enums.Gender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMvcDto {
    private Long id;
    private String name;
    private String surname;
    private byte[] photo;
    private String email;

    private Gender gender;
    private String phone;
}
