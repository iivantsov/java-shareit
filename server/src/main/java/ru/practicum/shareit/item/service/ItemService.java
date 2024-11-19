package ru.practicum.shareit.item.service;

import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemSaveDto;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(long userId, ItemSaveDto itemSaveDto);

    CommentDto addComment(long userId, long itemId, CommentSaveDto commentSaveDto);

    ItemDto updateItem(long userId, long itemId, ItemSaveDto itemSaveDto);

    ItemDto getItem(long itemId);

    Collection<ItemDto> getAllOwnerItems(long userId);

    Collection<ItemDto> searchItems(String text);
}
