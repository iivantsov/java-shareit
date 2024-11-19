package ru.practicum.shareit.dto.user;

import jakarta.validation.constraints.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSaveDto {
    @NotNull(groups = {CreateRequest.class})
    @Size(min = 1, max = 50, groups = {CreateRequest.class, UpdateRequest.class})
    String name;

    @NotNull(groups = {CreateRequest.class})
    @Email(groups = {CreateRequest.class, UpdateRequest.class})
    @Size(min = 1, max = 150, groups = {CreateRequest.class, UpdateRequest.class})
    String email;
}
