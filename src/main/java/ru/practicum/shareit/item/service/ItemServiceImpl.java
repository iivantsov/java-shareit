package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponseDto addItem(long userId, ItemRequestDto itemRequestDto) {
        userRepository.validateUserPresence(userId);
        Item mappedItem = itemMapper.map(itemRequestDto);
        mappedItem.setOwner(userId);
        Item item = itemRepository.addItem(mappedItem);
        return itemMapper.map(item);
    }

    @Override
    public ItemResponseDto updateItem(long userId, long itemId, ItemRequestDto itemRequestDto) {
        userRepository.validateUserPresence(userId);
        Item item = itemRepository.updateItem(itemId, itemRequestDto);
        return itemMapper.map(item);
    }

    @Override
    public ItemResponseDto getItem(long userId, long itemId) {
        userRepository.validateUserPresence(userId);
        Item item = itemRepository.getItem(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        return itemMapper.map(item);
    }

    @Override
    public Collection<ItemResponseDto> getAllItems(long userId) {
        userRepository.validateUserPresence(userId);
        Collection<Item> items = itemRepository.getAllItems(userId);
        return itemMapper.map(items);
    }

    @Override
    public Collection<ItemResponseDto> searchItems(long userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        userRepository.validateUserPresence(userId);
        Collection<Item> items = itemRepository.searchItems(text);
        return itemMapper.map(items);
    }
}
