package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Item addItem(Item item);

    Item updateItem(long itemId, ItemRequestDto itemRequestDto);

    Optional<Item> getItem(long itemId);

    Collection<Item> getAllItems(long userId);

    Collection<Item> searchItems(String text);
}
