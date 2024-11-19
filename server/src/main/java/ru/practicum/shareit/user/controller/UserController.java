package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserSaveDto;
import ru.practicum.shareit.user.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiResourses.USERS)
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    UserDto createUser(@RequestBody UserSaveDto userSaveDto) {
        return userService.createUser(userSaveDto);
    }

    @GetMapping("/{userId}")
    UserDto getUser(@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@PathVariable Long userId,
                       @RequestBody UserSaveDto userSaveDto) {
        return userService.updateUser(userId, userSaveDto);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
