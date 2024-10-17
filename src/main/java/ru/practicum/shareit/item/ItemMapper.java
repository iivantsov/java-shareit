package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;

@Mapper
public interface ItemMapper {

    ItemDto itemToItemDto(Item item);
}
