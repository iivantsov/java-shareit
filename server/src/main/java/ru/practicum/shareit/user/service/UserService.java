package ru.practicum.shareit.user.service;

import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserSaveDto;

public interface UserService {

    UserDto createUser(UserSaveDto userSaveDto);

    UserDto getUser(Long userId);

    UserDto updateUser(Long userId, UserSaveDto userSaveDto);

    void deleteUser(Long userId);
}
