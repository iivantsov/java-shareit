package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.dto.booking.BookingStatus;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.dto.user.UserDto;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private final BookingService service;
    private BookingDto bookingExpected;

    @BeforeEach
    public void testInit() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(1);

        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setName("laptop");
        item.setDescription("Apple laptop for working on java ShareIt project");
        item.setAvailable(true);

        UserDto booker = new UserDto();
        booker.setId(1L);
        booker.setName("Booker");
        booker.setEmail("booker@yandex.ru");

        bookingExpected = new BookingDto();
        bookingExpected.setId(1L);
        bookingExpected.setStart(start);
        bookingExpected.setEnd(end);
        bookingExpected.setStatus(BookingStatus.WAITING);
        bookingExpected.setItem(item);
        bookingExpected.setBooker(booker);
    }

    @Test
    public void testAddBookingReturnsExpectedBooking() throws Exception {
        // Given
        long userId = 1L;
        BookingSaveDto bookingSaveDto = new BookingSaveDto();
        bookingSaveDto.setItemId(1L);
        bookingSaveDto.setStart(LocalDateTime.now());
        bookingSaveDto.setStart(LocalDateTime.now().plusMinutes(1));
        String bookingSaveDtoJson = objectMapper.writeValueAsString(bookingSaveDto);
        String bookingDtoExpectedJson = objectMapper.writeValueAsString(bookingExpected);
        // When
        when(service.addBooking(eq(userId), any(BookingSaveDto.class)))
                .thenReturn(bookingExpected);
        // Then
        mockMvc.perform(post(ApiResourses.BOOKINGS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingSaveDtoJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDtoExpectedJson));

        verify(service, times(1)).addBooking(eq(userId), any(BookingSaveDto.class));
    }

    @Test
    public void testManageBookingWithApproveReturnsApprovedBooking() throws Exception {
        // Given
        long userId = 1L;
        long bookingId = 1L;
        boolean approved = true;
        String path = ApiResourses.BOOKINGS + "/" + bookingId;
        // When
        when(service.manageBooking(eq(userId), eq(bookingId), eq(approved)))
                .thenAnswer(invocationOnMock -> {
                    bookingExpected.setStatus(BookingStatus.APPROVED);
                    return bookingExpected;
                });
        // Then
        mockMvc.perform(patch(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .param("approved", String.valueOf(approved))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.name())));

        verify(service, times(1)).manageBooking(eq(userId), eq(bookingId), eq(approved));
    }

    @Test
    public void testGetBookingReturnsExpectedBooking() throws Exception {
        // Given
        long userId = 1L;
        long bookingId = 1L;
        String path = ApiResourses.BOOKINGS + "/" + bookingId;
        String bookingDtoExpectedJson = objectMapper.writeValueAsString(bookingExpected);
        // When
        when(service.getBooking(eq(userId), eq(bookingId)))
                .thenReturn(bookingExpected);
        // Then
        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(bookingDtoExpectedJson));

        verify(service, times(1)).getBooking(eq(userId), eq(bookingId));
    }

    @Test
    public void testGetAllUserBookingsWithRejectedStateReturnsEmpty() throws Exception {
        // Given
        long userId = 1L;
        BookingState state = BookingState.REJECTED;
        // When
        when(service.getAllUserBookings(eq(userId), eq(state)))
                .thenReturn(Collections.emptyList());
        // Then
        mockMvc.perform(get(ApiResourses.BOOKINGS)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .param("state", String.valueOf(state))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(service, times(1)).getAllUserBookings(eq(userId), eq(state));
    }

    @Test
    public void testGetAllUserItemsBookingsWithWaitingStateReturnsListOfSingleExpectedBooking() throws Exception {
        // Given
        long userId = 1L;
        BookingState state = BookingState.WAITING;
        String path = ApiResourses.BOOKINGS + "/owner";
        List<BookingDto> expectedBookings = List.of(bookingExpected);
        String expectedBookingsJson = objectMapper.writeValueAsString(expectedBookings);
        // When
        when(service.getAllUserItemsBookings(eq(userId), eq(state)))
                .thenReturn(expectedBookings);
        // Then
        mockMvc.perform(get(path)
                        .header(RequestHttpHeaders.USER_ID, userId)
                        .param("state", String.valueOf(state))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(content().json(expectedBookingsJson));

        verify(service, times(1)).getAllUserItemsBookings(eq(userId), eq(state));
    }
}
