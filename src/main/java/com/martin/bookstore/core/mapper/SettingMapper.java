package com.martin.bookstore.core.mapper;

import com.martin.bookstore.entity.Setting;
import com.martin.bookstore.dto.request.SettingRequestDto;
import com.martin.bookstore.dto.response.SettingResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface SettingMapper {

    Setting asEntity(SettingRequestDto dto);

    SettingResponseDto asOutput(Setting entity);

    List<SettingResponseDto> asOutput(List<Setting> entities);

    void update(@MappingTarget Setting entity, SettingRequestDto dto);
}
