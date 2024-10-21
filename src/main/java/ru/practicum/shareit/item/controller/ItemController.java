package ru.practicum.shareit.item.controller;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
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
    ItemResponseDto addItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                            @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemService.addItem(userId, itemRequestDto);
    }

    @PatchMapping("/{itemId}")
    ItemResponseDto updateItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                               @PathVariable long itemId,
                               @RequestBody ItemRequestDto itemRequestDto) {
        return itemService.updateItem(userId, itemId, itemRequestDto);
    }

    @GetMapping("/{itemId}")
    ItemResponseDto getItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId, @PathVariable long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    Collection<ItemResponseDto> getAllItems(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    Collection<ItemResponseDto> searchItems(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                            @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }
}
