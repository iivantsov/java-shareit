package ru.practicum.shareit.item.controller;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
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
    Item addItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    Item updateItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                    @PathVariable long itemId,
                    @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    Item getItem(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId, @PathVariable long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    Collection<Item> getAllItems(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    Collection<Item> searchItems(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                 @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }
}
