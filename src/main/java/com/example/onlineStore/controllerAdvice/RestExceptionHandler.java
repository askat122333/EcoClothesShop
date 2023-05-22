package com.example.onlineStore.controllerAdvice;

import com.example.onlineStore.dto.ErrMessage;
import com.example.onlineStore.exceptions.ProductNotFoundException;
import com.example.onlineStore.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

}
