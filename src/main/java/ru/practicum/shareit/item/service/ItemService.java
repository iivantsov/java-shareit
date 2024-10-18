package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Item addItem(long userId, ItemDto itemDto);

    Item updateItem(long userId, long itemId, ItemDto itemDto);

    Item getItem(long userId, long itemId);

    Collection<Item> getAllItems(long userId);

    Collection<Item> searchItems(long userId, String text);
}
