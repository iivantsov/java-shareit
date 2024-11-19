package ru.practicum.shareit.dto.booking;

import java.util.Arrays;
import java.util.Optional;

public enum BookingState {
	WAITING,
	REJECTED,
	CURRENT,
	PAST,
	FUTURE,
	ALL;

	public static Optional<BookingState> fromString(String state) {
		return Arrays.stream(BookingState.values())
				.filter(s -> s.name().equalsIgnoreCase(state))
				.findFirst();
	}
}
