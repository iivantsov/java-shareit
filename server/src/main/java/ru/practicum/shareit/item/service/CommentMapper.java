package ru.practicum.shareit.item.service;

import org.mapstruct.Mapping;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.service.UserMapper;

import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface CommentMapper {

    Comment map(CommentSaveDto commentSaveDto);

    Collection<CommentDto> map(Collection<Comment> comment);

    @Mapping(source = "author", target = "authorName", qualifiedByName = "userToName")
    CommentDto map(Comment comment);
}
