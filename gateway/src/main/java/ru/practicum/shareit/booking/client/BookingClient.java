package ru.practicum.shareit.booking.client;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.dto.booking.BookingSaveDto;
import ru.practicum.shareit.dto.booking.BookingState;
import ru.practicum.shareit.BaseClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String BOOKING_ID_PATH = "/{bookingId}";
    private static final String MANAGE_BOOKING_PATH = BOOKING_ID_PATH + "?approved={approved}";
    private static final String GET_ALL_USER_BOOKINGS_PATH = "?state={state}&from={from}&size={size}";
    private static final String GET_ALL_USER_ITEMS_BOOKINGS_PATH = "/owner/?state={state}";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ApiResourses.BOOKINGS))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addBooking(Long userId, BookingSaveDto bookingSaveDto) {
        return post("", userId, bookingSaveDto);
    }

    public ResponseEntity<Object> manageBooking(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> uriVariables = Map.of("bookingId",bookingId,"approved", approved);
        return patch(MANAGE_BOOKING_PATH, userId, uriVariables);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        Map<String, Object> uriVariables = Map.of("bookingId", bookingId);
        return get(BOOKING_ID_PATH, userId, uriVariables);
    }

    public ResponseEntity<Object> getAllUserBookings(Long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> uriVariables = Map.of("state", state.name(), "from", from, "size", size);
        return get(GET_ALL_USER_BOOKINGS_PATH, userId, uriVariables);
    }

    public ResponseEntity<Object> getAllUserItemsBookings(Long userId, BookingState state) {
        Map<String, Object> uriVariables = Map.of("state", state.name());
        return get(GET_ALL_USER_ITEMS_BOOKINGS_PATH, userId, uriVariables);
    }
}
