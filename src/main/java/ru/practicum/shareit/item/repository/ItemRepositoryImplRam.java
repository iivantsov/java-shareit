package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Predicate;

@Repository
public class ItemRepositoryImplRam implements ItemRepository {
    /**
     * <K> userId, V - List<Item>
     */
    private final Map<Long, List<Item>> userItems = new LinkedHashMap<>();
    private long nextId = 0L;

    @Override
    public Item addItem(Item item) {
        long itemId = getNextId();
        item.setId(itemId);
        List<Item> items = userItems.computeIfAbsent(item.getOwner(), v -> new ArrayList<>());
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(long itemId, ItemRequestDto itemRequestDto) {
        Item item = getItem(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));

        String name = itemRequestDto.getName();
        if (name != null && !name.isBlank()) {
            item.setName(name);
        }
        String description = itemRequestDto.getDescription();
        if (description != null && !description.isBlank()) {
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
        return userItems.values().stream()
                .flatMap(List::stream)
                .filter(item -> item.getId() == itemId)
                .findFirst();
    }

    @Override
    public Collection<Item> getAllItems(long userId) {
        return userItems.get(userId);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        String textL = text.toLowerCase();

        Predicate<Item> itemSearchFilter = item ->
                ((item.getName().toLowerCase().contains(textL) || item.getDescription().toLowerCase().contains(textL)))
                        && item.getAvailable();

        return userItems.values().stream()
                .flatMap(List::stream)
                .filter(itemSearchFilter)
                .toList();
    }

    private long getNextId() {
        return ++nextId;
    }
}
