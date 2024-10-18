package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        userService.validateUserPresence(userId);
        Item item = itemMapper.map(itemDto);
        item.setOwner(userId);
        return itemRepository.addItem(item);
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.validateUserPresence(userId);
        return itemRepository.updateItem(itemId, itemDto);
    }

    @Override
    public Item getItem(long userId, long itemId) {
        userService.validateUserPresence(userId);
        return itemRepository.getItem(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
    }

    @Override
    public Collection<Item> getAllItems(long userId) {
        userService.validateUserPresence(userId);
        return itemRepository.getAllItems(userId);
    }

    @Override
    public Collection<Item> searchItems(long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        userService.validateUserPresence(userId);
        return itemRepository.searchItems(text);
    }
}
