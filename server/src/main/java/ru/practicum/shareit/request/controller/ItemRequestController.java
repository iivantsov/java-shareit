package ru.practicum.shareit.request.controller;

import ru.practicum.shareit.api.ApiResourses;
import ru.practicum.shareit.api.RequestHttpHeaders;
import ru.practicum.shareit.dto.request.ItemRequestDto;
import ru.practicum.shareit.dto.request.ItemRequestSaveDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = ApiResourses.REQUESTS)
@AllArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId,
                                            @RequestBody ItemRequestSaveDto itemRequestSaveDto) {
        return itemRequestService.createItemRequest(userId, itemRequestSaveDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllUserItemRequest(@RequestHeader(RequestHttpHeaders.USER_ID) Long userId) {
        return itemRequestService.getAllUserItemRequest(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests() {
        return itemRequestService.getAllItemRequests();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(long requestId) {
        return itemRequestService.getItemRequest(requestId);
    }
}
