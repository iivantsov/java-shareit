package ru.practicum.shareit.user;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper
public interface UserMapper {

    UserDto userToUserDto(User user);
}
