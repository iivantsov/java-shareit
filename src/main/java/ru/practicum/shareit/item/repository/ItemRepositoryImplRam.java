package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;

@Repository
@AllArgsConstructor
public class ItemRepositoryImplRam implements ItemRepository {
    /**
     * <K> - itemId, <V> - Item
     */
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        long id = getNextId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, ItemRequestDto itemRequestDto) {
        Item item = getItem(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));

        String name = itemRequestDto.getName();
        if (name != null) {
            item.setName(name);
        }
        String description = itemRequestDto.getDescription();
        if (description != null) {
            item.setDescription(description);
        }
        Boolean available = itemRequestDto.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }

        return item;
    }

    @Override
    public Optional<Item> getItem(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Collection<Item> getAllItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner() == userId)
                .toList();
    }

    @Override
    public Collection<Item> searchItems(String text) {
        String textL = text.toLowerCase();

        Predicate<Item> itemSearchFilter = item ->
                ((item.getName().toLowerCase().contains(textL) || item.getDescription().toLowerCase().contains(textL)))
                        && item.getAvailable();

        return items.values().stream()
                .filter(itemSearchFilter)
                .toList();
    }

    private long getNextId() {
        long nextId = items.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);
        return ++nextId;
    }
}
