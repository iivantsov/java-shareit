package ru.practicum.shareit.user.service;

import ru.practicum.shareit.exception.NotFoundException;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User createUser(UserDto userDto) {
        User user = userMapper.map(userDto);
        return userRepository.createUser(user);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        return userRepository.updateUser(userId, userDto);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUser(userId);
    }
}
