package ru.practicum.shareit.item.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final CommentRepository commentRepo;
    private final BookingRepository bookingRepo;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public ItemDto addItem(long userId, ItemSaveDto itemSaveDto) {
        User owner = userRepo.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));
        Item item = itemMapper.map(itemSaveDto);
        item.setOwner(owner);
        Item savedItem = itemRepo.save(item);

        return itemMapper.map(savedItem);
    }

    @Override
    public CommentDto addComment(long userId, long itemId, CommentSaveDto commentSaveDto) {
        Booking booking = bookingRepo.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now())
                .stream()
                .filter(b -> b.getItem().getId() == itemId)
                .findFirst()
                .orElseThrow(() -> new NotValidException(Comment.class, "cannot be posted. Please check user id " +
                        userId + " has previously booked item id " + itemId));

        Comment comment = commentMapper.map(commentSaveDto);
        comment.setAuthor(booking.getBooker());
        comment.setItem(booking.getItem());
        Comment savedComment = commentRepo.save(comment);

        return commentMapper.map(savedComment);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemSaveDto itemSaveDto) {
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenException("item could be updated only by owner");
        }
        itemMapper.updateItemFromDto(item, itemSaveDto);
        Item savedItem = itemRepo.save(item);

        return itemMapper.map(savedItem);
    }

    @Override
    public ItemDto getItem(long itemId) {
        Item item = itemRepo.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        ItemDto itemDto = itemMapper.map(item);

        Collection<LocalDateTime> futureBookings =
                bookingRepo.findAllByItemIdAndStartAfterOrderByStartAsc(itemId, LocalDateTime.now()).stream()
                        .map((Booking::getStart))
                        .toList();

        LocalDateTime nextBooking = findNextBooking(futureBookings);
        itemDto.setNextBooking(nextBooking);
        LocalDateTime lastBooking = findLastBooking(futureBookings);
        itemDto.setLastBooking(lastBooking);

        Collection<CommentDto> commentsDto = commentMapper.map(commentRepo.findAllByItemId(itemId));
        itemDto.setComments(commentsDto);

        return itemDto;
    }

    @Override
    public Collection<ItemDto> getAllOwnerItems(long userId) {
        Map<Long, ItemDto> idToItemDto = itemRepo.findAllByOwnerId(userId).stream()
                .map(itemMapper::map)
                .collect(Collectors.toMap(ItemDto::getId, Function.identity()));

        Collection<Booking> futureBookings =
                bookingRepo.findAllByItemIdInAndStartAfterOrderByStartAsc(idToItemDto.keySet(), LocalDateTime.now());

        for (ItemDto itemDto : idToItemDto.values()) {
            Collection<LocalDateTime> futureItemBookings = futureBookings.stream()
                    .filter(booking -> booking.getItem().getId() == itemDto.getId())
                    .map(Booking::getStart)
                    .toList();

            LocalDateTime nextBooking = findNextBooking(futureItemBookings);
            itemDto.setNextBooking(nextBooking);
            LocalDateTime lastBooking = findLastBooking(futureItemBookings);
            itemDto.setLastBooking(lastBooking);
        }

        Collection<Comment> comments = commentRepo.findAllByItemIdIn(idToItemDto.keySet());
        for (Comment comment : comments) {
            long id = comment.getItem().getId();
            ItemDto itemDto = idToItemDto.get(id);
            itemDto.getComments().add(commentMapper.map(comment));
        }

        return idToItemDto.values();
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        Collection<Item> items = itemRepo.searchItems(text);
        return itemMapper.map(items);
    }

    private LocalDateTime findNextBooking(Collection<LocalDateTime> futureBookings) {
        return futureBookings.stream()
                .reduce((first, last) -> first)
                .orElse(null);
    }

    private LocalDateTime findLastBooking(Collection<LocalDateTime> futureBookings) {
        return futureBookings.stream()
                .reduce((first, last) -> last)
                .orElse(null);
    }
}
