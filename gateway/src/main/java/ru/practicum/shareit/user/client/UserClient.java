package ru.practicum.shareit.user.client;

import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.dto.user.UserSaveDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class UserClient extends BaseClient {

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + ApiResourses.USERS))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> createUser(UserSaveDto userSaveDto) {
        return post("", userSaveDto);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        String path = "/" + userId.toString();
        return get(path);
    }

    public ResponseEntity<Object> updateUser(Long userId, UserSaveDto userSaveDto) {
        String path = "/" + userId.toString();
        return patch(path, userSaveDto);
    }

    public void deleteUser(Long userId) {
        String path = "/" + userId.toString();
        delete(path);
    }
}
