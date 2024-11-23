package ru.practicum.shareit.request.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;
import ru.practicum.shareit.request.client.ItemRequestClient;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiResourses.REQUESTS)
@AllArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    /**
     * Create a request for the new item by description
     */
    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                                                    @RequestBody @Valid ItemRequestSaveDto itemRequestSaveDto) {
        return itemRequestClient.createItemRequest(userId, itemRequestSaveDto);
    }

    /**
     * Get all user's requests
     */
    @GetMapping
    public ResponseEntity<Object> getAllUserItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return itemRequestClient.getAllUserItemRequest(userId);
    }

    /**
     * Get all requests created by other users
     */
    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequests() {
        return itemRequestClient.getAllItemRequests();
    }

    /**
     * Get requests by id
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable @NotNull Long requestId) {
        return itemRequestClient.getItemRequest(requestId);
    }
}
