package com.example.onlineStore.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDto {
    private String email;
    private String subject;
    private String message;
}
