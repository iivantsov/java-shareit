package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse exceptionHandler(Exception exception) {
        log.error("exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationExceptionResponse> requestBodyExceptionHandler(MethodArgumentNotValidException exception) {
        List<ValidationExceptionResponse> response = exception.getBindingResult().getFieldErrors().stream()
                .map(er -> new ValidationExceptionResponse(er.getObjectName(), er.getField(), er.getDefaultMessage()))
                .toList();
        log.error("request body validation exception - {}", response);
        return response;
    }

    @ExceptionHandler(NotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse notValidExceptionHandler(NotValidException exception) {
        log.error("not valid exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }
}
