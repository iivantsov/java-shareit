package ru.practicum.shareit.booking;

public enum BookingStatus {
    /**
     * New booking, awaiting approval
     */
    WAITING,
    /**
     * Booking approved by owner
     */
    APPROVED,
    /**
     * Booking rejected by owner
     */
    REJECTED,
    /**
     * Booking canceled by user
     */
    CANCELED
}