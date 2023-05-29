package com.example.onlineStore.controllerAdvice;

import com.example.onlineStore.dto.errMessage.ErrMessage;
import com.example.onlineStore.dto.errMessage.ErrValidMessage;
import com.example.onlineStore.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrMessage> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrMessage> handleUserNotFoundException(UserNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrMessage> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PaymentCardNotFoundException.class)
    public ResponseEntity<ErrMessage> handlePaymentCardNotFoundException(PaymentCardNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrMessage> handleOrderNotFoundException(OrderNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrMessage> handleCartNotFoundException(CartNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrMessage> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrMessage> handleConstraintNotFoundException(ConstraintViolationException ex) {
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ValidException.class)
    public ResponseEntity<ErrValidMessage> handleValidNotFoundException(ValidException ex) {
        ErrValidMessage errMessage = new ErrValidMessage(HttpStatus.BAD_REQUEST.value(), ex.getMsg());
        return new ResponseEntity<>(errMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<ErrMessage> handleDiscountNotFoundException(DiscountNotFoundException ex){
        ErrMessage errMessage = new ErrMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errMessage,HttpStatus.NOT_FOUND);
    }
}
