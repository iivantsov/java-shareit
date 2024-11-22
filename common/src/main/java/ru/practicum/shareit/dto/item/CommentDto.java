package ru.practicum.shareit.dto.item;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    long id;
    String text;
    String authorName;
    LocalDateTime created;
}
