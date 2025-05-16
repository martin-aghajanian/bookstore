package com.martin.bookstore.controller;

import com.martin.bookstore.criteria.UserSearchCriteria;
import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.dto.response.UserResponseDto;
import com.martin.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('admin:read')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<UserResponseDto> getAll(UserSearchCriteria criteria) {
        return userService.getAll(criteria);
    }

}
