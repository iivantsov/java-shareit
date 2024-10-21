package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User createUser(User user);

    Optional<User> getUser(Long userId);

    default void validateUserPresence(long userId) {
        getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    }

    User updateUser(Long userId, UserRequestDto userRequestDto);

    void deleteUser(Long userId);
}
