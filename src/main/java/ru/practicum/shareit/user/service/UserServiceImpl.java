package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User mappedUser = userMapper.map(userRequestDto);
        User user = userRepository.createUser(mappedUser);
        return userMapper.map(user);
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        return userMapper.map(user);
    }

    @Override
    public void validateUserPresence(long userId) {
        userRepository.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    }

    @Override
    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.updateUser(userId, userRequestDto);
        return userMapper.map(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
