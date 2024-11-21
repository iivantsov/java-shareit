package ru.practicum.shareit.request.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final ItemRequestService service;
    private ItemRequestDto itemRequestExpected;

    @BeforeEach
    public void testInit() {
        itemRequestExpected = new ItemRequestDto();
        itemRequestExpected.setId(1L);
        itemRequestExpected.setDescription("description");
    }

    @Test
    public void testCreateItemRequestReturnsExpectedRequest() throws Exception {
        // Given
        long userId = 1L;
        ItemRequestSaveDto itemRequest = new ItemRequestSaveDto();
        itemRequest.setDescription(itemRequestExpected.getDescription());
        String itemRequestJson = objectMapper.writeValueAsString(itemRequest);
        String itemRequestExpectedJson = objectMapper.writeValueAsString(itemRequestExpected);
        // When
        when(service.createItemRequest(any(Long.class), any(ItemRequestSaveDto.class)))
                .thenReturn(itemRequestExpected);
        // Then
        mockMvc.perform(post(ApiResourses.REQUESTS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(itemRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemRequestExpectedJson));

        verify(service, times(1))
                .createItemRequest(any(Long.class), any(ItemRequestSaveDto.class));
    }

    @Test
    public void testGetAllUserItemRequestReturnsCollectionOfSingleExpectedRequest() throws Exception {
        // Given
        long userId = 10;
        Collection<ItemRequestDto> itemRequestsExpected = List.of(itemRequestExpected);
        String itemRequestsExpectedJson = objectMapper.writeValueAsString(itemRequestsExpected);
        // When
        when(service.getAllUserItemRequest(eq(userId)))
                .thenReturn(itemRequestsExpected);
        // Then
        mockMvc.perform(get(ApiResourses.REQUESTS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemRequestsExpectedJson));

        verify(service, times(1)).getAllUserItemRequest(eq(userId));
    }

    @Test
    public void testGetAllItemRequestsReturnsEmptyCollection() throws Exception {
        // Given
        String path = ApiResourses.REQUESTS + "/all";
        // When
        when(service.getAllItemRequests())
                .thenReturn(List.of());
        // Then
        mockMvc.perform(get(path)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).getAllItemRequests();
    }

    @Test
    public void testGetItemRequestReturnsExpectedItemRequest() throws Exception {
        // Given
        long itemRequestId = itemRequestExpected.getId();
        String path = ApiResourses.REQUESTS + "/" + itemRequestId;
        String itemRequestExpectedJson = objectMapper.writeValueAsString(itemRequestExpected);
        // When
        when(service.getItemRequest(eq(itemRequestId)))
                .thenReturn(itemRequestExpected);
        // Then
        mockMvc.perform(get(path)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(itemRequestExpectedJson));

        verify(service, times(1)).getItemRequest(eq(itemRequestId));
    }
}
