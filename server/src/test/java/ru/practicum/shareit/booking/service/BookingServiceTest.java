package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.BookingStatus;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.user.UserDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final EntityManager em;
    private final BookingService service;
    private final BookingMapper mapper;

    private Booking bookingExpected;
    private Item itemExpected;
    private User bookerExpected;

    private static final long NON_EXISTENT_ID = 999L;
    private static final long UNAVAILABLE_ITEM_ID = 50L; // Predefined in data.sql

    @BeforeEach
    public void testInit() {
        itemExpected = new Item();
        itemExpected.setId(30L);

        bookerExpected = new User();
        bookerExpected.setId(30L);

        bookingExpected = new Booking();
        bookingExpected.setId(1L);
        bookingExpected.setItem(itemExpected);
        bookingExpected.setBooker(bookerExpected);
        bookingExpected.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void testAddBookingSuccessful() throws Exception {
        // Given
        long userId = bookerExpected.getId();
        BookingSaveDto bookingSaveDto = new BookingSaveDto();
        bookingSaveDto.setItemId(itemExpected.getId());
        bookingSaveDto.setStart(bookingExpected.getStart());
        bookingSaveDto.setEnd(bookingExpected.getEnd());
        // When
        service.addBooking(userId, bookingSaveDto);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking AS b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingExpected.getId()).getSingleResult();
        BookingDto bookingDto = mapper.map(booking);
        // Then
        BookingDto bookingDtoExpected = mapper.map(bookingExpected);
        assertThat(bookingDto, allOf(
                hasProperty("id",
                        equalTo(bookingDtoExpected.getId())),
                hasProperty("start",
                        equalTo(bookingDtoExpected.getStart())),
                hasProperty("end",
                        equalTo(bookingDtoExpected.getEnd())),
                hasProperty("item",
                        allOf(hasProperty("id", equalTo(bookingDtoExpected.getItem().getId())))),
                hasProperty("booker",
                        allOf(hasProperty("id", equalTo(bookingDtoExpected.getBooker().getId())))),
                hasProperty("status",
                        equalTo(bookingDtoExpected.getStatus()))
        ));
    }

    @Test
    public void testAddBookingNonExistentItemThrowsNotFoundException() {
        // Given
        long userId = bookerExpected.getId();
        BookingSaveDto bookingSaveDto = new BookingSaveDto();
        // When
        bookingSaveDto.setItemId(NON_EXISTENT_ID);
        bookingSaveDto.setStart(bookingExpected.getStart());
        bookingSaveDto.setEnd(bookingExpected.getEnd());
        // Then
        assertThrows(NotFoundException.class, () -> service.addBooking(userId, bookingSaveDto));
    }

    @Test
    public void testAddBookingNonExistentBookerThrowsNotFoundException() {
        // Given
        long userId;
        BookingSaveDto bookingSaveDto = new BookingSaveDto();
        // When
        userId = NON_EXISTENT_ID;
        bookingSaveDto.setItemId(itemExpected.getId());
        bookingSaveDto.setStart(bookingExpected.getStart());
        bookingSaveDto.setEnd(bookingExpected.getEnd());
        // Then
        assertThrows(NotFoundException.class, () -> service.addBooking(userId, bookingSaveDto));
    }

    @Test
    public void testAddBookingUnavailableItemThrowsNotValidException() {
        // Given
        long userId = bookerExpected.getId();
        BookingSaveDto bookingSaveDto = new BookingSaveDto();
        // When
        bookingSaveDto.setItemId(UNAVAILABLE_ITEM_ID);
        bookingSaveDto.setStart(bookingExpected.getStart());
        bookingSaveDto.setEnd(bookingExpected.getEnd());
        // Then
        assertThrows(NotValidException.class, () -> service.addBooking(userId, bookingSaveDto));
    }

    @Test
    public void testManageBookingWithTrueApprovedParameterUpdatesBookingStatusToApproved() throws Exception {
        // Given
        long userId = 10;
        long bookingId = 10;
        boolean approved = true;
        // When
        service.manageBooking(userId, bookingId, approved);
        TypedQuery<Booking> query = em.createQuery("SELECT b FROM Booking AS b WHERE b.id = :id", Booking.class);
        Booking booking = query.setParameter("id", bookingId).getSingleResult();
        BookingDto bookingDto = mapper.map(booking);
        // Then
        long bookerExpectedId = 20; // Predefined in data.sql
        assertThat(bookingDto, allOf(
                hasProperty("id", equalTo(bookingId)),
                hasProperty("booker", allOf(hasProperty("id", equalTo(bookerExpectedId)))),
                hasProperty("status", equalTo(BookingStatus.APPROVED))
        ));
    }

    @Test
    public void testManageBookingWithTrueApprovedParameterAntNotByOwnerThrowsNotValidException() {
        // Given
        long userId = 10;
        long bookingId = 20;
        boolean approved = true;
        // When, Then
        assertThrows(NotValidException.class, () -> service.manageBooking(userId, bookingId, approved));
    }

    @Test
    public void testGetBookingSuccessful() {
        // Given
        long userId = 20;
        long bookingId = 10;
        // When
        BookingDto bookingDto = service.getBooking(userId, bookingId);
        // Then
        BookingDto bookingId10DtoExpected = new BookingDto();
        bookingId10DtoExpected.setId(10L);
        LocalDateTime start = LocalDateTime.of(2024, 11, 21, 10, 10, 10);
        bookingId10DtoExpected.setStart(start);
        LocalDateTime end = LocalDateTime.of(2024, 11, 21, 11, 11, 11);
        bookingId10DtoExpected.setEnd(end);
        ItemDto item = new ItemDto();
        item.setId(10L);
        bookingId10DtoExpected.setItem(item);
        UserDto booker = new UserDto();
        booker.setId(20L);
        bookingId10DtoExpected.setBooker(booker);
        bookingId10DtoExpected.setStatus(BookingStatus.APPROVED);

        assertThat(bookingDto, allOf(
                hasProperty("id",
                        equalTo(bookingId10DtoExpected.getId())),
                hasProperty("start",
                        equalTo(bookingId10DtoExpected.getStart())),
                hasProperty("end",
                        equalTo(bookingId10DtoExpected.getEnd())),
                hasProperty("item",
                        allOf(hasProperty("id", equalTo(bookingId10DtoExpected.getItem().getId())))),
                hasProperty("booker",
                        allOf(hasProperty("id", equalTo(bookingId10DtoExpected.getBooker().getId())))),
                hasProperty("status",
                        equalTo(bookingId10DtoExpected.getStatus()))
        ));
    }

    @Test
    public void testGetBookingByNotTheOwnerThrowsNotValidException() {
        // Given
        long userId = 70L;
        long bookingId = 10L;
        // When, Then
        assertThrows(NotValidException.class, () -> service.getBooking(userId, bookingId));
    }

    @Test
    void testGetBookingThatNotExist() throws Exception {
        // Given
        long userId = 10L;
        long bookingId = NON_EXISTENT_ID;
        // When, Then
        assertThrows(NotFoundException.class, () -> service.getBooking(userId, bookingId));
    }

    @Test
    void testGetAllUserItemsBookingsWithAllStateParameterForNonExistentUserThrowsNotFoundException() {
        // Given
        long userId = NON_EXISTENT_ID;
        BookingState state = BookingState.ALL;
        // When, Then
        assertThrows(NotFoundException.class, () -> service.getAllUserItemsBookings(userId, state));
    }

    @Test
    void testGetAllUserItemsBookingsWithWaitingStateParameterResponsesCollectionHasNotNullProperties() {
        // Given
        long userId = 20L;
        BookingState state = BookingState.WAITING;
        // When
        Collection<BookingDto> bookings = service.getAllUserItemsBookings(userId, state);
        // Then
        assertCollectionBookingDtoHasNotNullProperties(bookings);
    }

    @Test
    void testGetAllUserItemsBookingsWithRejectedStateParameterResponsesNotNullCollection() {
        // Given
        long userId = 20L;
        BookingState state = BookingState.REJECTED;
        // When
        Collection<BookingDto> bookings = service.getAllUserItemsBookings(userId, state);
        // Then
        assertCollectionBookingDtoHasNotNullProperties(bookings);
    }

    @Test
    void testGetAllUserItemsBookingsWithCurrentStateParameterResponsesNotNullCollection() {
        // Given
        long userId = 20L;
        BookingState state = BookingState.CURRENT;
        // When
        Collection<BookingDto> bookings = service.getAllUserItemsBookings(userId, state);
        // Then
        assertCollectionBookingDtoHasNotNullProperties(bookings);
    }

    @Test
    void testGetAllUserItemsBookingsWithPastStateParameterResponsesNotNullCollection() {
        // Given
        long userId = 20L;
        BookingState state = BookingState.PAST;
        // When
        Collection<BookingDto> bookings = service.getAllUserItemsBookings(userId, state);
        // Then
        assertCollectionBookingDtoHasNotNullProperties(bookings);
    }

    @Test
    void testGetAllUserItemsBookingsWithFutureStateParameterResponsesNotNullCollection() {
        // Given
        long userId = 20L;
        BookingState state = BookingState.FUTURE;
        // When
        Collection<BookingDto> bookings = service.getAllUserItemsBookings(userId, state);
        // Then
        assertCollectionBookingDtoHasNotNullProperties(bookings);
    }

    @Test
    void testGetAllUserItemsBookingsWithAllStateParameterResponsesNotNullCollection() {
        // Given
        long userId = 20L;
        BookingState state = BookingState.ALL;
        // When
        Collection<BookingDto> bookings = service.getAllUserItemsBookings(userId, state);
        // Then
        assertCollectionBookingDtoHasNotNullProperties(bookings);
    }

    private void assertCollectionBookingDtoHasNotNullProperties(Collection<BookingDto> bookings) {
        bookings.forEach(booking -> assertThat(booking, allOf(
                hasProperty("id", notNullValue()),
                hasProperty("start", nullValue()),
                hasProperty("end", nullValue()),
                hasProperty("item", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("name", notNullValue()),
                        hasProperty("description", notNullValue()),
                        hasProperty("available", notNullValue())
                )),
                hasProperty("booker", allOf(
                        hasProperty("id", notNullValue()),
                        hasProperty("email", notNullValue()),
                        hasProperty("name", notNullValue())
                )),
                hasProperty("status", notNullValue()))
        ));
    }
}
