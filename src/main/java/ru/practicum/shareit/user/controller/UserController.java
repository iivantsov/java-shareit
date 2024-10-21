package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.service.UserService;

import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    UserResponseDto createUser(@RequestBody @Validated(CreateRequest.class) UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/{userId}")
    UserResponseDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    UserResponseDto updateUser(@PathVariable Long userId,
                               @RequestBody @Validated(UpdateRequest.class) UserRequestDto userRequestDto) {
        return userService.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
