package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImplRam implements UserRepository {
    /**
     * Regular Expression by RFC 5322 for Email Validation
     */
    private static final String EMAIL_REGEX_VALIDATION_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        checkEmailConflict(user.getEmail());
        long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User updateUser(Long userId, UserDto userDto) {
        User user = getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        String name = userDto.getName();
        if (name != null) {
            user.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null) {
            validateEmail(email);
            checkEmailConflict(email);
            user.setEmail(email);
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private long getNextId() {
        long nextId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0L);
        return ++nextId;
    }

    private void validateEmail(String email) {
        if (!Pattern.compile(EMAIL_REGEX_VALIDATION_PATTERN).matcher(email).matches()) {
            throw new NotValidException("user email " + email + " is not valid");
        }
    }

    private void checkEmailConflict(String email) {
        users.values().stream()
                .map(User::getEmail)
                .filter(e -> e.equals(email))
                .findAny()
                .ifPresent(e -> {
                    throw new ConflictException("email " + e + " already exist");
                });
    }
}
