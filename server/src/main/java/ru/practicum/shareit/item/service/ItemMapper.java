package ru.practicum.shareit.item.service;

import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemResponseToRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.dto.item.ItemSaveDto;

import org.mapstruct.*;
import ru.practicum.shareit.user.service.UserMapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ItemMapper {

    Item map(ItemSaveDto itemSaveDto);

    ItemDto map(Item item);

    Collection<ItemResponseToRequestDto> mapToResponseToRequest(Collection<Item> items);

    @Mapping(source = "owner", target = "ownerId", qualifiedByName = "userToId")
    ItemResponseToRequestDto mapToResponseToRequest(Item item);

    Collection<ItemDto> map(Collection<Item> items);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(@MappingTarget Item item, ItemSaveDto itemDto);
}
