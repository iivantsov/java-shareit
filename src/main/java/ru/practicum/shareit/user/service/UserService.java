package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    User createUser(UserDto userDto);

    User getUser(Long userId);

    void validateUserPresence(long userId);

    User updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}
