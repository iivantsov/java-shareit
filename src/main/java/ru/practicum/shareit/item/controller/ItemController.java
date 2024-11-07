package ru.practicum.shareit.item.controller;

import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    ItemDto addItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                    @RequestBody @Valid ItemSaveDto itemSaveDto) {
        return itemService.addItem(userId, itemSaveDto);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @PathVariable long itemId,
                          @RequestBody @Valid CommentSaveDto commentSaveDto) {
        return itemService.addComment(userId, itemId, commentSaveDto);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                       @PathVariable long itemId,
                       @RequestBody ItemSaveDto itemSaveDto) {
        return itemService.updateItem(userId, itemId, itemSaveDto);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    Collection<ItemDto> getAllOwnerItems(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return itemService.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
