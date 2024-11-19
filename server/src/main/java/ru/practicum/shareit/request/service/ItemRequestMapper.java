package ru.practicum.shareit.request.service;

import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;
import ru.practicum.shareit.request.model.ItemRequest;

import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    ItemRequest map(ItemRequestSaveDto itemRequestSaveDto);

    ItemRequestDto map(ItemRequest itemRequest);

    Collection<ItemRequestDto> map(Collection<ItemRequest> itemRequests);
}
