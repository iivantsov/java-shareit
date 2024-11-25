package ru.practicum.shareit.booking.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.exception.NotValidException;

import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(path = ApiResourses.BOOKINGS)
@RequiredArgsConstructor
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	/**
	 * Add new booking (any user)
	 */
	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
											 @RequestBody @Valid BookingSaveDto bookingSaveDto) {
		return bookingClient.addBooking(userId, bookingSaveDto);
	}

	/**
	 * Approve or reject booking (only by owner)
	 */
	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> manageBooking(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
												@PathVariable Long bookingId,
												@RequestParam Boolean approved) {
		return bookingClient.manageBooking(userId, bookingId, approved);
	}

	/**
	 * Get particular booking (only by booking author or item owner)
	 */
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
											 @PathVariable Long bookingId) {
		return bookingClient.getBooking(userId, bookingId);
	}

	/**
	 * Get all bookings with required state for current user
	 */
	@GetMapping
	public ResponseEntity<Object> getAllUserBookings(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
													 @RequestParam(defaultValue = "all") String state,
													 @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
													 @RequestParam(defaultValue = "10") @Positive Integer size) {
		BookingState bookingState = BookingState.fromString(state)
				.orElseThrow(() -> new NotValidException(BookingState.class, state + " not valid"));
		return bookingClient.getAllUserBookings(userId, bookingState, from, size);
	}

	/**
	 * Get all bookings with required state for items of current user
	 */
	@GetMapping("/owner")
	public ResponseEntity<Object> getAllUserItemsBookings(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
														  @RequestParam(defaultValue = "all") String state) {
		BookingState bookingState = BookingState.fromString(state)
				.orElseThrow(() -> new NotValidException(BookingState.class, state + " not valid"));
		return bookingClient.getAllUserItemsBookings(userId, bookingState);
	}
}
