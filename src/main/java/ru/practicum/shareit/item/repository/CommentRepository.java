package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
