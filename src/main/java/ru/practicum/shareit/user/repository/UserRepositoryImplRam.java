package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.user.dto.UserRequestDto;
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
    private long nextId = 0;

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
    public User updateUser(Long userId, UserRequestDto userRequestDto) {
        User user = getUser(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        String name = userRequestDto.getName();
        if (name != null) {
            user.setName(name);
        }
        String email = userRequestDto.getEmail();
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
