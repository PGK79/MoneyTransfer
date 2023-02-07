package ru.netology.moneytransferservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.exceptions.TransferException;

@RestControllerAdvice
public class AllExceptionHandlers {
    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<String> inputDataExceptionHandler(InputDataException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<String> transferExceptionHandler(TransferException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}