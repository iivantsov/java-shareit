package ru.practicum.shareit.request.service;

import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;

import java.util.Collection;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(long userId, ItemRequestSaveDto itemRequestSaveDto);

    Collection<ItemRequestDto> getAllUserItemRequest(long userId);

    Collection<ItemRequestDto> getAllItemRequests();

    ItemRequestDto getItemRequest(long requestId);
}
