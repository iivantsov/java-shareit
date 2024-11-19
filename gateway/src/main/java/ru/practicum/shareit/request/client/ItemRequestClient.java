package ru.practicum.shareit.request.client;

import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String GET_ALL_ITEM_REQUESTS_PATH = "/all";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ApiResourses.REQUESTS))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> createItemRequest(Long userId, ItemRequestSaveDto itemRequestSaveDto) {
        return post("", userId, itemRequestSaveDto);
    }

    public ResponseEntity<Object> getAllUserItemRequest(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getAllItemRequests() {
        return get(GET_ALL_ITEM_REQUESTS_PATH);
    }

    public ResponseEntity<Object> getItemRequest(Long requestId) {
        String path = "/" + requestId;
        return get(path);
    }
}
