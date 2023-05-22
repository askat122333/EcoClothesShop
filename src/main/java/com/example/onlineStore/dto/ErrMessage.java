package com.example.onlineStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrMessage {

    private int statusCode;
    private String message;
}
