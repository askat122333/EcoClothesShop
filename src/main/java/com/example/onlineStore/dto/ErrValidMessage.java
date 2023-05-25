package com.example.onlineStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ErrValidMessage {
    private int statusCode;
    private List<String> message;
}
