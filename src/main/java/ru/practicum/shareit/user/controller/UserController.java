package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserSaveDto;
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
    UserDto createUser(@RequestBody @Validated(CreateRequest.class) UserSaveDto userSaveDto) {
        return userService.createUser(userSaveDto);
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@PathVariable Long userId,
                       @RequestBody @Validated(UpdateRequest.class) UserSaveDto userSaveDto) {
        return userService.updateUser(userId, userSaveDto);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
