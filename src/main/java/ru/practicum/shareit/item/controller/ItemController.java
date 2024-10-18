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
    Item addItem(@RequestHeader(value = "X-Sharer-User-Id") long userId, @RequestBody @Valid ItemDto itemDto) {
        return itemService.addItem(userId, itemDto);
    }

    @PatchMapping("/{item-id}")
    Item updateItem(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                    @PathVariable("item-id") long itemId,
                    @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{item-id}")
    Item getItem(@RequestHeader(value = "X-Sharer-User-Id") long userId, @PathVariable("item-id") long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    Collection<Item> getAllItems(@RequestHeader(value = "X-Sharer-User-Id") long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/search")
    Collection<Item> searchItems(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                 @RequestParam String text) {
        return itemService.searchItems(userId, text);
    }
}
