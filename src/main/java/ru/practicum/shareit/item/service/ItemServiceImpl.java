package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSaveDto;
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
    public ItemDto addItem(long userId, ItemSaveDto itemSaveDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Item item = itemMapper.map(itemSaveDto);
        item.setOwner(owner);
        Item savedItem = itemRepository.save(item);
        return itemMapper.map(savedItem);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemSaveDto itemSaveDto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenException("item could be updated only by owner");
        }
        itemMapper.updateItemFromDto(item, itemSaveDto);
        Item savedItem = itemRepository.save(item);
        return itemMapper.map(savedItem);
    }

    @Override
    public ItemDto getItem(long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        return itemMapper.map(item);
    }

    @Override
    public Collection<ItemDto> getAllItems(long userId) {
        Collection<Item> items = itemRepository.findAllByOwnerId(userId);
        return itemMapper.map(items);
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepository.searchItems(text);
        return itemMapper.map(items);
    }
}
