package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.dto.user.UserSaveDto;
import ru.practicum.shareit.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final UserService service;
    private UserDto userExpected;

    @BeforeEach
    void testInit() {
        userExpected = new UserDto();
        userExpected.setId(1L);
        userExpected.setName("User1");
        userExpected.setEmail("user1@yandex.ru");
    }

    @Test
    void testCreateUserReturnsExpectedUser() throws Exception {
        // Given
        long userId = userExpected.getId();
        UserSaveDto userSaveDto = new UserSaveDto();
        userSaveDto.setName(userExpected.getName());
        userSaveDto.setEmail(userExpected.getEmail());
        String userSaveDtoJson = objectMapper.writeValueAsString(userSaveDto);
        // When
        when(service.createUser(any(UserSaveDto.class)))
                .thenReturn(userExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(userExpected);
        // Then
        mockMvc.perform(post(ApiResourses.USERS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userSaveDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).createUser(any(UserSaveDto.class));
    }

    @Test
    void testGetUserReturnsExpectedUser() throws Exception {
        // Given
        long userId = userExpected.getId();
        String path = ApiResourses.USERS + "/" + userId;
        // When
        when(service.getUser(eq(userId)))
                .thenReturn(userExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(userExpected);
        // Then
        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).getUser(eq(userId));
    }

    @Test
    void testUpdateUserNameAndEmailReturnsUpdatedUser() throws Exception {
        // Given
        long userId = userExpected.getId();
        UserSaveDto userSaveDtoForUpdate = new UserSaveDto();
        String nameUpdated = "Superuser";
        userSaveDtoForUpdate.setName(nameUpdated);
        String emailUpdated = "superuser@yandex.ru";
        userSaveDtoForUpdate.setEmail(emailUpdated);
        String userSaveDtoForUpdateJson = objectMapper.writeValueAsString(userSaveDtoForUpdate);
        String path = ApiResourses.USERS + "/" + userId;
        // When
        when(service.updateUser(eq(userId), eq(userSaveDtoForUpdate)))
                .thenAnswer(invocationOnMock -> {
                    userExpected.setName(nameUpdated);
                    userExpected.setEmail(emailUpdated);
                    return userExpected;
                });
        // Then
        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userSaveDtoForUpdateJson))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userExpected)));

        verify(service, times(1)).updateUser(eq(userId), eq(userSaveDtoForUpdate));
    }

    @Test
    void testDeleteUserReturnOK() throws Exception {
        // Given
        long userId = userExpected.getId();
        String path = ApiResourses.USERS + "/" + userId;
        // When, Then
        mockMvc.perform(delete(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(eq(userId));
    }
}