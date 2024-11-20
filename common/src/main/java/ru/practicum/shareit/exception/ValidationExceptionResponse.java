package ru.practicum.shareit.exception;

public record ValidationExceptionResponse(String object, String field, String error) { }
