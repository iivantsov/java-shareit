package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @NotNull
    String name;
    @NotNull
    String description;
    Boolean available;
}
