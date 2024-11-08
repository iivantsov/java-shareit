package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto addBooking(long userId, BookingSaveDto bookingSaveDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));

        long itemId = bookingSaveDto.getItemId();
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(Item.class, itemId));
        if (!item.isAvailable()) {
            throw new NotValidException(Item.class, "is not available for booking");
        }

        Booking booking = bookingMapper.map(bookingSaveDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        Booking savedBooking = bookingRepository.save(booking);
        log.info("booking saved to repo - {}", savedBooking);

        return bookingMapper.map(savedBooking);
    }

    @Override
    public BookingDto manageBooking(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Booking.class, bookingId));

        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotValidException(Booking.class, "can only be approved by the owner of the item");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.map(savedBooking);
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(Booking.class, bookingId));

        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotValidException(Booking.class, "owner id " + booking.getItem().getOwner().getId() +
                    " or booker id " + booking.getBooker().getId() + " does not match with user id " + userId);
        }

        return bookingMapper.map(booking);
    }

    @Override
    public Collection<BookingDto> getAllUserBookings(long userId, BookingState state) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class, userId));

        final Collection<Booking> bookings;
        final LocalDateTime current = LocalDateTime.now();
        switch (state) {
            case WAITING -> {
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
            }
            case CURRENT -> {
                bookings = bookingRepository.findAllCurrentBookings(userId, current);
            }
            case PAST -> {
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, current);
            }
            case FUTURE -> {
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, current);
            }
            case ALL -> {
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
            }
            default -> {
                throw new NotValidException(BookingState.class, "invalid");
            }
        }

        return bookingMapper.map(bookings);
    }

    @Override
    public Collection<BookingDto> getAllUserItemsBookings(long userId, BookingState state) {
        Collection<Long> itemIds = itemRepository.findAllByOwnerId(userId).stream()
                .map(Item::getId)
                .toList();

        return getAllUserBookings(userId, state).stream()
                .filter(booking -> itemIds.contains(booking.getItem().getId()))
                .toList();
    }
}
