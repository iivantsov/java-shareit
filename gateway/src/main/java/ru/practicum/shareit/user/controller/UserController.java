package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.UpdateRequest;
import ru.practicum.shareit.dto.user.UserSaveDto;
import ru.practicum.shareit.user.client.UserClient;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
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
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        return userClient.getUser(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody @Validated(UpdateRequest.class) UserSaveDto userSaveDto) {
        return userClient.updateUser(userId, userSaveDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        userClient.deleteUser(userId);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}
