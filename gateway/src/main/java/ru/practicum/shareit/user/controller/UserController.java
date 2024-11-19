package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;
import ru.practicum.shareit.dto.user.UserSaveDto;
import ru.practicum.shareit.user.client.UserClient;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiResourses.USERS)
@AllArgsConstructor
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Validated(CreateRequest.class) UserSaveDto userSaveDto) {
        return userClient.createUser(userSaveDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable @NotNull Long userId) {
        return userClient.getUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable @NotNull Long userId,
                                             @RequestBody @Validated(UpdateRequest.class) UserSaveDto userSaveDto) {
        return userClient.updateUser(userId, userSaveDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable @NotNull Long userId) {
        userClient.deleteUser(userId);
    }
}
