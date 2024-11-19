package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import ru.practicum.shareit.dto.request.ItemRequestDto;

@Mapper
public interface ItemRequestMapper {

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);
}
