package ru.practicum.shareit.dto.item;

import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentSaveDto {
    @NotNull(groups = {CreateRequest.class})
    @Size(min = 1, max = 300, groups = {CreateRequest.class, UpdateRequest.class})
    String text;
}
