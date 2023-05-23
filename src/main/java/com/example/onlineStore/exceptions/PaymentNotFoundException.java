package com.example.onlineStore.exceptions;

public class PaymentNotFoundException extends Exception{
    public PaymentNotFoundException(String msg){
        super(msg);
    }
}
