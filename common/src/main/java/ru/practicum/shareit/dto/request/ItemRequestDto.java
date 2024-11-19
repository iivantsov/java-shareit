package ru.practicum.shareit.dto.request;

import ru.practicum.shareit.dto.user.UserDto;

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
    UserDto requester;
}
