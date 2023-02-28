package ru.netology.moneytransferservice.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netology.moneytransferservice.exception.ConfirmationException;
import ru.netology.moneytransferservice.exception.InputDataException;
import ru.netology.moneytransferservice.exception.RepositoryException;
import ru.netology.moneytransferservice.exception.TransferException;
import ru.netology.moneytransferservice.model.ExceptionDto;

@RestControllerAdvice
public class AllExceptionHandlers {
    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<ExceptionDto> inputDataExceptionHandler(InputDataException e) {
        e.getLoggerSimple().logFile(e.getMessage());
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), e.getId()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<ExceptionDto> transferExceptionHandler(TransferException e) {
        e.getLogger().logFile(e.getMessage());
        return new ResponseEntity<>(new ExceptionDto(e.getMessage(), e.getId()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConfirmationException.class)
    public ResponseEntity<String> ConfirmationExceptionHandler(ConfirmationException e) {
        e.getLoggerSimple().logFile(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<String> RepositoryExceptionHandler(RepositoryException e) {
        e.getLoggerSimple().logFile(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}