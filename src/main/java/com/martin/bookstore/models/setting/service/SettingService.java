package com.martin.bookstore.models.setting.service;

import com.martin.bookstore.models.setting.dto.SettingDto;

import java.util.List;

public interface SettingService {

    List<SettingDto> getAllSettings();

    SettingDto getSettingById(Long id);

    SettingDto createSetting(SettingDto settingDto);

    SettingDto updateSetting(Long id, SettingDto updatedSettingDto);

    void deleteSetting(Long id);

}
