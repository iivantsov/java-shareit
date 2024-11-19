package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.dto.booking.BookingDto;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.booking.BookingState;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = ApiResourses.BOOKINGS)
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestHeader(RequestHttpHeaders.USER_ID) long userId,
                                 @RequestBody BookingSaveDto bookingSaveDto) {
        return bookingService.addBooking(userId, bookingSaveDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto manageBooking(@RequestHeader(RequestHttpHeaders.USER_ID) long userId, @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.manageBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(RequestHttpHeaders.USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getAllUserBookings(@RequestHeader(RequestHttpHeaders.USER_ID) long userId,
                                                     @RequestParam(defaultValue = "all") BookingState state) {
        return bookingService.getAllUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllUserItemsBookings(@RequestHeader(RequestHttpHeaders.USER_ID) long userId,
                                                          @RequestParam(defaultValue = "all") BookingState state) {
        return bookingService.getAllUserItemsBookings(userId, state);
    }
}
