package ru.practicum.shareit.request.service;

import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository itemRequestRepo;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    public ItemRequestDto createItemRequest(long userId, ItemRequestSaveDto itemRequestSaveDto) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        ItemRequest itemRequest = itemRequestMapper.map(itemRequestSaveDto);
        itemRequest.setRequester(user);
        ItemRequest savedItemRequest = itemRequestRepo.save(itemRequest);

        return itemRequestMapper.map(savedItemRequest);
    }

    @Override
    public Collection<ItemRequestDto> getAllUserItemRequest(long userId) {
        return itemRequestMapper.map(itemRequestRepo.findAllByRequesterIdOrderByCreatedDesc(userId));
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequests() {
        return itemRequestMapper.map(itemRequestRepo.findAll(Sort.by(Sort.Direction.DESC, "created")));
    }

    @Override
    public ItemRequestDto getItemRequest(long requestId) {
        ItemRequest itemRequest = itemRequestRepo.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ItemRequest.class, requestId));
        Collection<Item> items = itemRepo.findAllByRequestId(requestId);
        items.forEach(item -> log.debug("item id - {}, owner id - {}", item.getId(), item.getOwner().getId()));

        ItemRequestDto itemRequestDto = itemRequestMapper.map(itemRequest);
        itemRequestDto.setItems(itemMapper.mapToResponseToRequest(items));

        return itemRequestDto;
    }
}
