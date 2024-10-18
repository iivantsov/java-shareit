package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    Optional<User> getUser(Long userId);

    User updateUser(Long userId, UserDto userDto);

    void deleteUser(Long userId);
}
