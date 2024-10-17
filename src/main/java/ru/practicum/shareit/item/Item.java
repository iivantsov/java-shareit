package ru.practicum.shareit.item;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.request.ItemRequest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id;
    String name;
    String description;
    /**
     * Booking availability status
     */
    Boolean available;
    User owner;
    /**
     * Link to another User's Item Request (if it was created)
     */
    ItemRequest request;
}
