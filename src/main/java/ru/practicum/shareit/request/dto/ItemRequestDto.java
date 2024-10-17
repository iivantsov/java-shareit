package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.user.model.User;

import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @NotNull
    String description;
    @NotNull
    User requester;
}
