package ru.practicum.shareit.item.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemSaveDto;
import ru.practicum.shareit.item.service.ItemService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = ApiResourses.ITEMS)
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    ItemDto addItem(@RequestHeader(RequestHttpHeaders.USER_ID) long userId, @RequestBody ItemSaveDto itemSaveDto) {
        return itemService.addItem(userId, itemSaveDto);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@RequestHeader(RequestHttpHeaders.USER_ID) long userId, @PathVariable long itemId,
                          @RequestBody CommentSaveDto commentSaveDto) {
        return itemService.addComment(userId, itemId, commentSaveDto);
    }

    @PatchMapping("/{itemId}")
    ItemDto updateItem(@RequestHeader(RequestHttpHeaders.USER_ID) long userId, @PathVariable long itemId,
                       @RequestBody ItemSaveDto itemSaveDto) {
        return itemService.updateItem(userId, itemId, itemSaveDto);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @GetMapping
    Collection<ItemDto> getAllOwnerItems(@RequestHeader(RequestHttpHeaders.USER_ID) long userId) {
        return itemService.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    Collection<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }
}
