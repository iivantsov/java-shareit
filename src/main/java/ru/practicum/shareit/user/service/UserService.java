package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUser(Long userId);

    void validateUserPresence(long userId);

    UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto);

    void deleteUser(Long userId);
}
