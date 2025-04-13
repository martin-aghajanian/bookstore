package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.SettingDto;
import com.martin.bookstore.entity.Setting;
import org.springframework.stereotype.Component;

@Component
public class SettingMapper {

    public SettingDto toDto(Setting setting) {
        SettingDto settingDto = new SettingDto();
        settingDto.setId(setting.getId());
        settingDto.setName(setting.getName());
        return settingDto;
    }

    public Setting toEntity(SettingDto settingDto) {
        Setting setting = new Setting();
        setting.setId(settingDto.getId());
        setting.setName(settingDto.getName());
        return setting;
    }
}
