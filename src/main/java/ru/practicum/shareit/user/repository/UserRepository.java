package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default void validateUserPresence(long userId) {
        findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
    }
}
