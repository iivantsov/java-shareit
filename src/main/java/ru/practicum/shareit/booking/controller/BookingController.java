package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;

import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.api.RequestHttpHeaders;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    // Add new booking (any user)
    @PostMapping
    BookingDto addBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @RequestBody @Valid BookingSaveDto bookingSaveDto) {
        return bookingService.addBooking(userId, bookingSaveDto);
    }

    // Approve or reject booking (only by owner)
    @PatchMapping("/{bookingId}")
    BookingDto manageBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                             @PathVariable long bookingId, @RequestParam boolean approved) {
        return bookingService.manageBooking(userId, bookingId, approved);
    }

    // Get particular booking (only by booking author or item owner)
    @GetMapping("/{bookingId}")
    BookingDto getBooking(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                          @PathVariable long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    // Get all bookings with required state for current user
    @GetMapping
    Collection<BookingDto> getAllBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                          @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllBookings(userId, state);
    }

    // Get all bookings with required state for items of current user
    @GetMapping("/owner")
    Collection<BookingDto> getAllOwnersItemBookings(@RequestHeader(value = RequestHttpHeaders.USER_ID) long userId,
                                                    @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllOwnersItemBookings(userId, state);
    }
}
