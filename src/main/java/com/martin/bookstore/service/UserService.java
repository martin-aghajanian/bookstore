package com.martin.bookstore.service;

import com.martin.bookstore.criteria.UserSearchCriteria;
import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.dto.response.UserResponseDto;
import com.martin.bookstore.mapper.UserMapper;
import com.martin.bookstore.repository.UserRepository;
import com.martin.bookstore.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.asOutput(user);
    }

    public PageResponseDto<UserResponseDto> getAll(UserSearchCriteria criteria) {
        Page<UserResponseDto> page = userRepository.findAll(
                criteria,
                criteria.buildPageRequest()
        );
        return PageResponseDto.from(page);
    }
}
