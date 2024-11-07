package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Collection<Comment> findAllByItemId(long itemId);

    Collection<Comment> findAllByItemIdIn(Collection<Long> itemIds);
}
