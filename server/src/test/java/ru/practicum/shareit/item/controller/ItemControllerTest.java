package ru.practicum.shareit.item.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemSaveDto;
import ru.practicum.shareit.item.service.ItemService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final ItemService service;
    private ItemDto itemExpected;
    private CommentDto commentExpected;

    @BeforeEach
    public void testInit() {
        itemExpected = new ItemDto();
        itemExpected.setId(1L);
        itemExpected.setName("item");
        itemExpected.setDescription("description");
        itemExpected.setAvailable(false);

        commentExpected = new CommentDto();
        commentExpected.setId(1L);
        commentExpected.setText("comment");
        commentExpected.setAuthorName("user1");
    }

    @Test
    public void testAddItemReturnsExpectedItem() throws Exception {
        // Given
        long userId = 1L;
        ItemSaveDto itemSaveDto = new ItemSaveDto();
        itemSaveDto.setName(itemExpected.getName());
        itemSaveDto.setDescription(itemExpected.getDescription());
        itemSaveDto.setAvailable(itemExpected.isAvailable());
        String itemSaveDtoJson = objectMapper.writeValueAsString(itemSaveDto);
        String itemDtoExpectedJson = objectMapper.writeValueAsString(itemExpected);
        // When
        when(service.addItem(any(Long.class), any(ItemSaveDto.class)))
                .thenReturn(itemExpected);
        // Then
        mockMvc.perform(post(ApiResourses.ITEMS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(itemSaveDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().json(itemDtoExpectedJson));

        verify(service, times(1)).addItem(any(Long.class), any(ItemSaveDto.class));
    }

    @Test
    public void testAddCommentReturnsExpectedItemWithExpectedComment() throws Exception {
        // Given
        long userId = 1L;
        long itemId = itemExpected.getId();
        String path = ApiResourses.ITEMS + "/" + itemId + "/comment";
        CommentSaveDto comment = new CommentSaveDto();
        comment.setText(commentExpected.getText());
        String commentJson = objectMapper.writeValueAsString(comment);
        String commentExpectedJson = objectMapper.writeValueAsString(commentExpected);
        // When
        when(service.addComment(any(Long.class), eq(itemId), eq(comment)))
                .thenReturn(commentExpected);
        // Then
        mockMvc.perform(post(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isOk())
                .andExpect(content().json(commentExpectedJson));

        verify(service, times(1)).addComment(any(Long.class), eq(itemId), eq(comment));
    }

    @Test
    public void testUpdateItemReturnsExpectedItemUpdated() throws Exception {
        // Given
        long userId = 1L;
        long itemId = itemExpected.getId();
        String path = ApiResourses.ITEMS + "/" + itemId;
        ItemSaveDto itemSaveDtoForUpdate = new ItemSaveDto();
        itemSaveDtoForUpdate.setDescription("updated description");
        itemSaveDtoForUpdate.setAvailable(true);
        String itemSaveDtoForUpdateJson = objectMapper.writeValueAsString(itemSaveDtoForUpdate);
        // When
        when(service.updateItem(eq(userId), eq(itemId), any(ItemSaveDto.class)))
                .thenAnswer(invocationOnMock -> {
                    itemExpected.setDescription(itemSaveDtoForUpdate.getDescription());
                    itemExpected.setAvailable(itemSaveDtoForUpdate.getAvailable());
                    return itemExpected;
                });
        // Then
        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(itemSaveDtoForUpdateJson))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemExpected)));

        verify(service, times(1)).updateItem(eq(userId), eq(itemId), any(ItemSaveDto.class));
    }

    @Test
    public void testGetItemReturnsExpectedItem() throws Exception {
        // Given
        long userId = 1L;
        String path = ApiResourses.ITEMS + "/" + userId;
        // When
        when(service.getItem(eq(userId)))
                .thenReturn(itemExpected);
        String userDtoExpectedJson = objectMapper.writeValueAsString(itemExpected);
        // Then
        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(userDtoExpectedJson));

        verify(service, times(1)).getItem(eq(userId));
    }

    @Test
    public void testGetAllOwnerItemsReturnsListOfSingleExpectedItem() throws Exception {
        // Given
        long userId = 1L;
        //String path = ApiResourses.ITEMS + "/" + userId;
        List<ItemDto> itemsExpected = List.of(itemExpected);
        String itemsExpectedJson = objectMapper.writeValueAsString(itemsExpected);
        // When
        when(service.getAllOwnerItems(eq(userId)))
                .thenReturn(itemsExpected);
        // Then
        mockMvc.perform(get(ApiResourses.ITEMS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(content().json(itemsExpectedJson));

        verify(service, times(1)).getAllOwnerItems(eq(userId));
    }

    @Test
    public void testSearchItemsReturnsListOfSingleExpectedItem() throws Exception {
        // Given
        String path = ApiResourses.ITEMS + "/search";
        String text = itemExpected.getDescription();
        List<ItemDto> itemsExpected = List.of(itemExpected);
        String itemsExpectedJson = objectMapper.writeValueAsString(itemsExpected);
        // When
        when(service.searchItems(eq(text)))
                .thenReturn(itemsExpected);
        // Then
        mockMvc.perform(get(path)
                        .param("text", text)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(content().json(itemsExpectedJson));

        verify(service, times(1)).searchItems(eq(text));
    }
}
