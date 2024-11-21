package ru.practicum.shareit.user.service;

import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserSaveDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final EntityManager em;
    private final UserService service;
    private final UserMapper mapper;
    private UserDto userCreatedExpected;
    private UserDto userExistedExpected;

    private static final long NON_EXISTENT_ID = 999L;

    @BeforeEach
    public void testInit() {
        userCreatedExpected = new UserDto();
        userCreatedExpected.setId(1L);
        userCreatedExpected.setName("user");
        userCreatedExpected.setEmail("user@yandex.ru");
        // Predefined in data.sql
        userExistedExpected = new UserDto();
        userExistedExpected.setId(10L);
        userExistedExpected.setName("user1");
        userExistedExpected.setEmail("user1@yandex.ru");
    }

    @Test
    public void testCreateUserReturnsExpectedUser() {
        // Given
        UserSaveDto userSaveDto = new UserSaveDto();
        userSaveDto.setName(userCreatedExpected.getName());
        userSaveDto.setEmail(userCreatedExpected.getEmail());
        long userCreatedExpectedId = userCreatedExpected.getId();
        // When
        service.createUser(userSaveDto);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u where u.id = :userId", User.class);
        UserDto userCreated = mapper.map(query.setParameter("userId", userCreatedExpectedId).getSingleResult());
        //Then
        assertThat(userCreated, allOf(
                hasProperty("id", equalTo(userCreatedExpectedId)),
                hasProperty("name", equalTo(userCreatedExpected.getName())),
                hasProperty("email", equalTo(userCreatedExpected.getEmail()))
        ));
    }

    @Test
    public void testGetUserReturnsExpectedUser() {
        // Given
        long userId = userExistedExpected.getId();
        // When
        UserDto user = service.getUser(userId);
        // Then
        assertThat(user, allOf(
                hasProperty("id", equalTo(userExistedExpected.getId())),
                hasProperty("name", equalTo(userExistedExpected.getName())),
                hasProperty("email", equalTo(userExistedExpected.getEmail()))
        ));
    }

    @Test
    public void testGetUserThatNotExistThrowsNotFoundException() {
        // Given
        long userId = NON_EXISTENT_ID;
        // When, Then
        assertThrows(NotFoundException.class, () -> service.getUser(userId));
    }

    @Test
    void testUpdateUserNameAndEmailReturnsUpdatedUser() {
        // Given
        long userId = 10;
        UserSaveDto userSaveDtoForUpdate = new UserSaveDto();
        userSaveDtoForUpdate.setName("user");
        userSaveDtoForUpdate.setEmail("user@yandex.ru");
        // When
        service.updateUser(userId, userSaveDtoForUpdate);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u where u.id = :userId", User.class);
        UserDto userUpdated = mapper.map(query.setParameter("userId", userId).getSingleResult());
        // Than
        assertThat(userUpdated, allOf(
                hasProperty("id", equalTo(userId)),
                hasProperty("name", equalTo(userSaveDtoForUpdate.getName())),
                hasProperty("email", equalTo(userSaveDtoForUpdate.getEmail()))
        ));
    }

    @Test
    public void testUpdateUserThatNotExistThrowsNotFoundException() {
        // Given
        long userId = NON_EXISTENT_ID;
        UserSaveDto userSaveDtoForUpdate = new UserSaveDto();
        userSaveDtoForUpdate.setName("user");
        userSaveDtoForUpdate.setEmail("user@yandex.ru");
        // When, Then
        assertThrows(NotFoundException.class, () -> service.updateUser(userId, userSaveDtoForUpdate));
    }

    @Test
    public void testDeleteUserRemovesUserFromRepository() {
        // Given
        long userId = 60;
        // When
        service.deleteUser(userId);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User AS u", User.class);
        // Then
        assertTrue(query.getResultStream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .isEmpty());
    }
}
