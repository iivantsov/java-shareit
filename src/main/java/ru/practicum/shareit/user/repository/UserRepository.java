package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    Optional<User> getUser(Long userId);

    User updateUser(Long userId, UserRequestDto userRequestDto);

    void deleteUser(Long userId);
}
