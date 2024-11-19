package ru.practicum.shareit.item.client;

import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.dto.item.CommentSaveDto;
import ru.practicum.shareit.dto.item.ItemSaveDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String ADD_COMMENT_PATH = "/{itemId}/comment";
    private static final String SEARCH_PATH = "/search?text=";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ApiResourses.ITEMS))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> addItem(Long userId, ItemSaveDto itemSaveDto) {
        return post("", userId, itemSaveDto);
    }

    public ResponseEntity<Object> addComment(Long userId, Long itemId, CommentSaveDto commentSaveDto) {
        Map<String, Object> uriVariables = Map.of("itemId", itemId);
        return post(ADD_COMMENT_PATH, userId, uriVariables, commentSaveDto);
    }

    public ResponseEntity<Object> updateItem(Long userId, Long itemId, ItemSaveDto itemSaveDto) {
        String path = "/" + itemId.toString();
        return patch(path, userId, itemSaveDto);
    }

    public ResponseEntity<Object> getItem(Long itemId) {
        String path = "/" + itemId.toString();
        return get(path);
    }

    public ResponseEntity<Object> getAllOwnerItems(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get(SEARCH_PATH + text);
    }
}
