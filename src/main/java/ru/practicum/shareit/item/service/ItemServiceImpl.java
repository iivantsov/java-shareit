package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Transactional
    @Override
    public ItemResponseDto addItem(long userId, ItemRequestDto itemRequestDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Item item = itemMapper.map(itemRequestDto);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);
        return itemMapper.map(savedItem);
    }

    @Transactional
    @Override
    public ItemResponseDto updateItem(long userId, long itemId, ItemRequestDto itemRequestDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenException("item could be updated only by owner");
        }
        itemMapper.updateItemFromDto(item, itemRequestDto);
        Item savedItem = itemRepository.save(item);
        return itemMapper.map(savedItem);
    }

    @Override
    public ItemResponseDto getItem(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        return itemMapper.map(item);
    }

    @Override
    public Collection<ItemResponseDto> getAllItems(long userId) {
        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        return itemMapper.map(items);
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepository.searchItems(text);
        return itemMapper.map(items);
    }
}
