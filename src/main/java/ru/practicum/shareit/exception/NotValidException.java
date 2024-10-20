package ru.practicum.shareit.exception;

public class NotValidException extends RuntimeException {

    public NotValidException(Class<?> entity, String field) {
        super(entity + " field " + field + " not valid");
    }
}
