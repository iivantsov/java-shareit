package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserSaveDto;
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
    public UserDto createUser(UserSaveDto userSaveDto) {
        User user = userMapper.map(userSaveDto);
        User savedUser = userRepository.save(user);
        return userMapper.map(savedUser);
    }

    @Override
    public UserDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        return userMapper.map(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserSaveDto userSaveDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        userMapper.updateUserFromDto(user, userSaveDto);
        User savedUser = userRepository.save(user);
        return userMapper.map(savedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
