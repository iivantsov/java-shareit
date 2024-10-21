package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.Collection;

public interface ItemService {

    ItemResponseDto addItem(long userId, ItemRequestDto itemRequestDto);

    ItemResponseDto updateItem(long userId, long itemId, ItemRequestDto itemRequestDto);

    ItemResponseDto getItem(long userId, long itemId);

    Collection<ItemResponseDto> getAllItems(long userId);

    Collection<ItemResponseDto> searchItems(long userId, String text);
}
