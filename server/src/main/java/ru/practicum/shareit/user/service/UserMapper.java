package ru.practicum.shareit.user.service;

import org.mapstruct.*;
import ru.practicum.shareit.dto.user.UserSaveDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(UserSaveDto userSaveDto);

    UserDto map(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(@MappingTarget User user, UserSaveDto userDto);

    @Named("userToName")
    static String userToName(User user) {
        return user.getName();
    }

    @Named("userToId")
    static long userToId(User user) {
        return user.getId();
    }
}
