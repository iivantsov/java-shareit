package ru.practicum.shareit.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    long id;
    String description;
    LocalDateTime created;
}