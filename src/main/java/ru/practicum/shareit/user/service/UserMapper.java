package ru.practicum.shareit.user.service;

import org.mapstruct.MappingTarget;
import ru.practicum.shareit.user.dto.UserSaveDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserSaveDto userSaveDto);

    UserDto map(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(@MappingTarget User user, UserSaveDto userDto);
}
