package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/{user-id}")
    User getUser(@PathVariable(value = "user-id") Long userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{user-id}")
    User updateUser(@PathVariable(value = "user-id") Long userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{user-id}")
    void deleteUser(@PathVariable(value = "user-id") Long userId) {
        userService.deleteUser(userId);
    }
}
