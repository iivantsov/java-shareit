package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.Collection;

public interface ItemService {

    ItemDto addItem(long userId, ItemSaveDto itemSaveDto);

    CommentDto addComment(long userId, long itemId, CommentSaveDto commentSaveDto);

    ItemDto updateItem(long userId, long itemId, ItemSaveDto itemSaveDto);

    ItemDto getItem(long itemId);

    Collection<ItemDto> getAllOwnerItems(long userId);

    Collection<ItemDto> searchItems(String text);
}
