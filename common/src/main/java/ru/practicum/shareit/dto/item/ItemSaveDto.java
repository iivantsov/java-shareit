package ru.practicum.shareit.dto.item;

import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemSaveDto {
    @NotNull(groups = {CreateRequest.class})
    @Size(min = 1, max = 50, groups = {CreateRequest.class, UpdateRequest.class})
    String name;

    @NotNull(groups = {CreateRequest.class})
    @Size(min = 1, max = 300, groups = {CreateRequest.class, UpdateRequest.class})
    String description;

    @NotNull(groups = {CreateRequest.class})
    Boolean available;

    Long requestId;
}
