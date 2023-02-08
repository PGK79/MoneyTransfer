package ru.netology.moneytransferservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.moneytransferservice.exceptions.InputDataException;
import ru.netology.moneytransferservice.exceptions.TransferException;
import ru.netology.moneytransferservice.models.ExceptionDto;

@RestControllerAdvice
public class AllExceptionHandlers {
    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ExceptionDto> inputDataExceptionHandler(InputDataException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), e.getId()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<ExceptionDto> transferExceptionHandler(TransferException e) {
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), e.getId()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}