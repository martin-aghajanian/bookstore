package com.martin.bookstore.mapper;

import com.martin.bookstore.entity.User;
import com.martin.bookstore.dto.request.UserRequestDto;
import com.martin.bookstore.dto.response.UserResponseDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    User asEntity(UserRequestDto userRequest);

    UserResponseDto asOutput(User user);

    List<UserResponseDto> asOutput(List<User> users);

    void update(@MappingTarget User entity, UserRequestDto userRequest);
}
