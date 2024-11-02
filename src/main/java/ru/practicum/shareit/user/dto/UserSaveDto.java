package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSaveDto {
    @NotBlank(groups = {CreateRequest.class})
    String name;
    @NotEmpty(groups = {CreateRequest.class})
    @Email(groups = {CreateRequest.class, UpdateRequest.class})
    String email;
}
