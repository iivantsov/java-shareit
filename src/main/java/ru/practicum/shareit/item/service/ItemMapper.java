package ru.practicum.shareit.item.service;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Collection;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item map(ItemRequestDto itemRequestDto);

    ItemResponseDto map(Item item);

    Collection<ItemResponseDto> map(Collection<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(@MappingTarget Item item, ItemRequestDto itemDto);
}
