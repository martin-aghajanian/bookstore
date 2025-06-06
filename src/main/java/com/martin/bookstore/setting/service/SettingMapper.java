package com.martin.bookstore.setting.service;

import com.martin.bookstore.setting.dto.SettingRequestDto;
import com.martin.bookstore.setting.dto.SettingResponseDto;
import com.martin.bookstore.setting.persistence.entity.Setting;
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
