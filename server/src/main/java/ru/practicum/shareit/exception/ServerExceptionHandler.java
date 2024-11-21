package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse exceptionHandler(Exception exception) {
        log.error("exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse forbiddenExceptionHandler(ForbiddenException exception) {
        log.error("forbidden exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(NotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse notValidExceptionHandler(NotValidException exception) {
        log.error("not valid exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundExceptionHandler(NotFoundException exception) {
        log.error("not found exception - {} ({})", exception.getMessage(), exception.getStackTrace()[0].toString());
        return new ExceptionResponse(exception.getMessage());
    }
}
