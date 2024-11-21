package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.dto.item.CommentDto;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.item.ItemSaveDto;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final EntityManager em;
    private final ItemService service;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private ItemDto itemAddedExpected;
    private ItemDto itemExistedExpected;
    private CommentDto commentAddedExpected;

    private static final long NON_EXISTENT_ID = 999L;

    @BeforeEach
    public void testInit() {
        itemAddedExpected = new ItemDto();
        itemAddedExpected.setId(1L);
        itemAddedExpected.setName("item");
        itemAddedExpected.setDescription("description");
        itemAddedExpected.setAvailable(false);

        commentAddedExpected = new CommentDto();
        commentAddedExpected.setId(1L);
        commentAddedExpected.setText("comment");

        // Predefined in data.sql
        itemExistedExpected = new ItemDto();
        //itemExistedExpected.setId(10L);
        itemExistedExpected.setId(40L);
        itemExistedExpected.setName("item4");
        itemExistedExpected.setDescription("description4");
        //itemExistedExpected.setAvailable(false);
        itemExistedExpected.setAvailable(true);
        CommentDto comment = new CommentDto();
        comment.setId(40);
        comment.setText("comment4");
        comment.setAuthorName("user5");
        itemExistedExpected.setComments(List.of(comment));
    }

    @Test
    public void testAddItemReturnsExpectedItem() {
        // Given
        long userId = 10L;
        ItemSaveDto item = new ItemSaveDto();
        item.setName(itemAddedExpected.getName());
        item.setDescription(itemAddedExpected.getDescription());
        item.setAvailable(itemAddedExpected.isAvailable());
        long itemAddedExpectedId = itemAddedExpected.getId();
        // When
        service.addItem(userId, item);
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item AS i WHERE i.id = :itemId", Item.class);
        ItemDto itemAdded = itemMapper.map(query.setParameter("itemId", itemAddedExpectedId).getSingleResult());
        // Then
        assertThat(itemAdded, allOf(
                hasProperty("id", equalTo(itemAddedExpected.getId())),
                hasProperty("name", equalTo(itemAddedExpected.getName())),
                hasProperty("description", equalTo(itemAddedExpected.getDescription())),
                hasProperty("available", equalTo(itemAddedExpected.isAvailable()))
        ));
    }

    @Test
    public void testAddItemForNonExistentUserThrowsNotFoundException() {
        // Given
        long userId = NON_EXISTENT_ID;
        ItemSaveDto item = new ItemSaveDto();
        item.setName(itemAddedExpected.getName());
        item.setDescription(itemAddedExpected.getDescription());
        item.setAvailable(itemAddedExpected.isAvailable());
        // When, Then
        assertThrows(NotFoundException.class, () -> service.addItem(userId, item));
    }

    @Test
    public void testAddItemForNonExistentRequestThrowsNotFoundException() {
        // Given
        long userId = 10;
        ItemSaveDto item = new ItemSaveDto();
        item.setName(itemAddedExpected.getName());
        item.setDescription(itemAddedExpected.getDescription());
        item.setAvailable(itemAddedExpected.isAvailable());
        item.setRequestId(NON_EXISTENT_ID);
        // When, Then
        assertThrows(NotFoundException.class, () -> service.addItem(userId, item));
    }

    @Test
    public void testAddCommentReturnsExpectedComment() {
        // Given
        long userId = 20L;
        long itemId = 50L;
        CommentSaveDto comment = new CommentSaveDto();
        comment.setText(commentAddedExpected.getText());
        // When
        CommentDto commentAdded = service.addComment(userId, itemId, comment);
        // Then
        assertThat(commentAdded, allOf(
                hasProperty("id", equalTo(commentAddedExpected.getId())),
                hasProperty("text", equalTo(commentAddedExpected.getText())
        )));
    }

    @Test
    public void testAddCommentForNonBookedItemThrowsNotValidException() {
        // Given
        long userId = 10L;
        long itemId = 10L;
        CommentSaveDto comment = new CommentSaveDto();
        comment.setText(commentAddedExpected.getText());
        // When, Then
        assertThrows(NotValidException.class, () -> service.addComment(userId, itemId, comment));
    }

    @Test
    public void testUpdateItemReturnsUpdatedItem() {
        // Given
        long userId = 10L;
        long itemId = 10L;
        ItemSaveDto itemForUpdate = new ItemSaveDto();
        itemForUpdate.setName("item");
        itemForUpdate.setDescription("description");
        itemForUpdate.setAvailable(false);
        // When
        service.updateItem(userId, itemId, itemForUpdate);
        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item AS i WHERE i.id = :itemId", Item.class);
        ItemDto itemUpdated = itemMapper.map(query.setParameter("itemId", itemId).getSingleResult());
        // Then
        assertThat(itemUpdated, allOf(
                hasProperty("id", equalTo(itemId)),
                hasProperty("name", equalTo(itemForUpdate.getName())),
                hasProperty("description", equalTo(itemForUpdate.getDescription())),
                hasProperty("available", equalTo(itemForUpdate.getAvailable()))
        ));
    }

    @Test
    public void testUpdateItemThatNotExistThrowsNotFoundException() {
        // Given
        long userId = 10L;
        long itemId = NON_EXISTENT_ID;
        ItemSaveDto itemForUpdate = new ItemSaveDto();
        itemForUpdate.setName("item");
        itemForUpdate.setDescription("description");
        itemForUpdate.setAvailable(false);
        // When, Then
        assertThrows(NotFoundException.class, () -> service.updateItem(userId, itemId, itemForUpdate));
    }

    @Test
    public void testUpdateItemByNotOwnerThrowsForbiddenException() {
        // Given
        long userId = 20L;
        long itemId = 10L;
        ItemSaveDto itemForUpdate = new ItemSaveDto();
        itemForUpdate.setName("item");
        itemForUpdate.setDescription("description");
        itemForUpdate.setAvailable(false);
        // When, Then
        assertThrows(ForbiddenException.class, () -> service.updateItem(userId, itemId, itemForUpdate));
    }

    @Test
    public void testGetItemReturnExpectedItem() {
        // Given
        long itemId = itemExistedExpected.getId();
        CommentDto commentExpected = itemExistedExpected.getComments().stream()
                .toList()
                .getFirst();
        // When
        ItemDto item = service.getItem(itemId);
        // Then
        assertThat(item, allOf(
                hasProperty("id", equalTo(itemExistedExpected.getId())),
                hasProperty("name", equalTo(itemExistedExpected.getName())),
                hasProperty("description", equalTo(itemExistedExpected.getDescription())),
                hasProperty("available", equalTo(itemExistedExpected.isAvailable()))
        ));

        List<CommentDto> comments = item.getComments()
                .stream()
                .toList();
        assertEquals(1, comments.size());
        assertThat(comments.getFirst(), allOf(
                hasProperty("id", equalTo(commentExpected.getId())),
                hasProperty("text", equalTo(commentExpected.getText())),
                hasProperty("authorName", equalTo(commentExpected.getAuthorName()))
        ));
    }

    @Test
    public void testGetNonExistentItemThrowsNotFoundException() {
        // Given
        long itemId = NON_EXISTENT_ID;
        // When, Then
        assertThrows(NotFoundException.class, () -> service.getItem(itemId));
    }

    @Test
    public void testGetAllOwnerItemsReturnsCollectionOfSingleExpectedItem() {
        // Given
        long userId = 40;
        // When
        List<ItemDto> items = service.getAllOwnerItems(userId)
                .stream()
                .toList();
        // Then
        assertEquals(1, items.size());
        ItemDto item = items.getFirst();
        assertThat(item, allOf(
                hasProperty("id", equalTo(itemExistedExpected.getId())),
                hasProperty("name", equalTo(itemExistedExpected.getName())),
                hasProperty("description", equalTo(itemExistedExpected.getDescription())),
                hasProperty("available", equalTo(itemExistedExpected.isAvailable()))
        ));
    }

    @Test
    public void testSearchItemsWithValidTextReturnsCollectionOfSingleExpectedItem() {
        // Given
        String text = "description4";
        // When
        List<ItemDto> items = service.searchItems(text)
                .stream()
                .toList();
        // Then
        assertEquals(1, items.size());
        ItemDto item = items.getFirst();
        assertThat(item, allOf(
                hasProperty("id", equalTo(itemExistedExpected.getId())),
                hasProperty("name", equalTo(itemExistedExpected.getName())),
                hasProperty("description", equalTo(itemExistedExpected.getDescription())),
                hasProperty("available", equalTo(itemExistedExpected.isAvailable()))
        ));
    }

    @Test
    public void testSearchItemsWithBlankTextReturnsEmptyCollection() {
        // Given
        String text = "";
        // When
        Collection<ItemDto> items = service.searchItems(text);
        // Then
        assertTrue(items.isEmpty());
    }
}
