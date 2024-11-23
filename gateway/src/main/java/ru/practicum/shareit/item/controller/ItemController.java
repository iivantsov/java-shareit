package ru.practicum.shareit.item.controller;

import org.springframework.stereotype.Controller;
import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.CreateRequest;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.api.UpdateRequest;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.dto.item.ItemSaveDto;
import ru.practicum.shareit.item.client.ItemClient;

import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = ApiResourses.ITEMS)
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                                          @RequestBody @Validated(CreateRequest.class) ItemSaveDto itemSaveDto) {
        return itemClient.addItem(userId, itemSaveDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                          @PathVariable @NotNull Long itemId,
                          @RequestBody @Validated(CreateRequest.class) CommentSaveDto commentSaveDto) {
        return itemClient.addComment(userId, itemId, commentSaveDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                       @PathVariable @NotNull Long itemId,
                       @RequestBody @Validated(UpdateRequest.class) ItemSaveDto itemSaveDto) {
        return itemClient.updateItem(userId, itemId, itemSaveDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable @NotNull Long itemId) {
        return itemClient.getItem(itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnerItems(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return itemClient.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam @NotNull String text) {
        return itemClient.searchItems(text);
    }
}