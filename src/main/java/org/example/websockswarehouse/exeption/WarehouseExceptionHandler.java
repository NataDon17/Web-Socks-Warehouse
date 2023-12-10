package org.example.websockswarehouse.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class WarehouseExceptionHandler {
    @ExceptionHandler(value = {SockNotFoundException.class})
    public ResponseEntity<?> handleSockNotFoundException() {
        String message = "The product is not in stock";
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {SockInvalidParameterException.class})
    public ResponseEntity<?> handleSockInvalidParameterException() {
        String message = "Invalid request parameter";
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleSQLException(SQLException sqlException) {
        String message = "Internal server error";
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
