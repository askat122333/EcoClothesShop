package com.example.onlineStore.exceptions;

public class PaymentCardNotFoundException extends Exception{
    public PaymentCardNotFoundException(String msg){
        super(msg);
    }
}
