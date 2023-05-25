package com.example.onlineStore.exceptions;

import lombok.Data;

import java.util.List;
@Data
public class ValidException extends RuntimeException  {

    private List<String> msg;
    public ValidException(List<String> msg){
        this.msg = msg;
    }
}
