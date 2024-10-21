# ShareIt App
### Author: Ilia I (iiv.88@yandex.ru)

### API

### users

#### Headers

- No custom request headers
- No custom response headers

#### DTOs

```
UserRequestDto {
    String name;
    String email;
}
```

```
UserResponseDto {
    long id;
    String name;
    String email;
}
```
#### Endpoints

1. **POST users/** _- Add users_
2. **GET user/{userId}** _- Get user by ID_
3. **PATCH user/{userId}** _- Update user_
4. **DELETE users/{userId}** _- Delete user_

### items

#### Headers

- Request header (required)
  - **X-Sharer-User-Id** _- ID of user that adds item_
- No custom response headers

#### DTOs

```
ItemRequestDto {
    String name;
    String description;
    Boolean available;
}
```

```
ItemResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
}
```

#### Endpoints

1. **POST items/** _- Add item. User (with ID from request header) that adds item becomes item's owner_
2. **GET items/** _- Get all items (this feature is available only for owners)_
3. **GET items/{itemId}** _- Get item by ID (this feature is available only for all users)_
4. **GET /items/search?text={text}** _- Get all items available for share with name and description containing the parameter "text"_
5. **PATCH items/{itemId}** _- Update item by ID (this feature is available only for owners)_

### Version history
### Version: 1.0
#### Initial implementation
- Added feature layout (models, DTOs, repositories, services, controllers)
- Implemented Item and User endpoints
- RAM repository implementation
