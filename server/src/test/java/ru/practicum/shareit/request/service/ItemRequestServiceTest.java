package ru.practicum.shareit.request.service;

import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final EntityManager em;
    private final ItemRequestService service;
    private final ItemRequestMapper mapper;
    private ItemRequestDto itemRequestCreatedExpected;
    private ItemRequestDto itemRequestExistedExpected;

    private static final long NON_EXISTENT_ID = 999L;

    @BeforeEach
    public void testInit() {
        itemRequestCreatedExpected = new ItemRequestDto();
        itemRequestCreatedExpected.setId(1L);
        itemRequestCreatedExpected.setDescription("description");
        // Predefined in data.sql
        itemRequestExistedExpected = new ItemRequestDto();
        itemRequestExistedExpected.setId(10L);
        itemRequestExistedExpected.setDescription("description1");
    }

    @Test
    public void testCreateItemRequestReturnsExpectedItemRequest() {
        // Given
        long userId = 10L;
        ItemRequestSaveDto itemRequest = new ItemRequestSaveDto();
        itemRequest.setDescription(itemRequestCreatedExpected.getDescription());
        long itemRequestExpectedId = itemRequestCreatedExpected.getId();
        // When
        service.createItemRequest(userId, itemRequest);
        TypedQuery<ItemRequest> query =
                em.createQuery("SELECT r FROM ItemRequest AS r WHERE r.id = :id", ItemRequest.class);
        ItemRequestDto itemRequestCreated = mapper
                .map(query.setParameter("id", itemRequestExpectedId).getSingleResult());
        // Then
        assertThat(itemRequestCreated, allOf(
                hasProperty("id", equalTo(itemRequestCreatedExpected.getId())),
                hasProperty("description", equalTo(itemRequestCreatedExpected.getDescription()))
        ));
    }

    @Test
    public void testCreateItemRequestByNonExistentUserThrowsNotFoundException() {
        // Given
        long userId = NON_EXISTENT_ID;
        ItemRequestSaveDto itemRequest = new ItemRequestSaveDto();
        itemRequest.setDescription(itemRequestCreatedExpected.getDescription());
        // When, Then
        assertThrows(NotFoundException.class, () -> service.createItemRequest(userId, itemRequest));
    }

    @Test
    public void testGetAllUserItemRequestReturnsCollectionOfSingleExpectedItemRequest() {
        // Given
        long userId = itemRequestExistedExpected.getId();
        // When
        List<ItemRequestDto> itemRequests = service.getAllUserItemRequest(userId)
                .stream()
                .toList();
        // Then
        assertEquals(itemRequests.size(), 1);
        assertThat(itemRequests.getFirst(), allOf(
                hasProperty("id", equalTo(itemRequestExistedExpected.getId())),
                hasProperty("description", equalTo(itemRequestExistedExpected.getDescription()))
        ));
    }

    @Test
    public void testGetAllItemRequestsReturnsCollectionOf5ExpectedItemRequests() {
        // Given
        long itemRequestExpectedId = 10;
        // When
        Collection<ItemRequestDto> itemRequests = service.getAllItemRequests();
        // Then
        assertEquals(itemRequests.size(), 5);
        for (ItemRequestDto request : itemRequests) {
            assertThat(request, allOf(
                    hasProperty("id", equalTo(itemRequestExpectedId)),
                    hasProperty("description", equalTo("description" + itemRequestExpectedId / 10))
            ));
            itemRequestExpectedId += 10;
        }
    }

    @Test
    public void testGetItemRequestReturnsExpectedItemRequest() {
        // Given
        long itemRequestId = itemRequestExistedExpected.getId();
        // When
        ItemRequestDto itemRequest = service.getItemRequest(itemRequestId);
        // Then
        assertThat(itemRequest, allOf(
                hasProperty("id", equalTo(itemRequestExistedExpected.getId())),
                hasProperty("description", equalTo(itemRequestExistedExpected.getDescription()))
        ));
    }

    @Test
    public void testGetItemRequestThatNotExistThrowsNotFoundException() {
        // Given
        long itemRequestId = NON_EXISTENT_ID;
        // When, Then
        assertThrows(NotFoundException.class, () -> service.getItemRequest(itemRequestId));
    }
}

