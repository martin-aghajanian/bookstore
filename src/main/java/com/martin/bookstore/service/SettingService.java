package com.martin.bookstore.service;

import com.martin.bookstore.dto.SettingDto;
import com.martin.bookstore.core.mapper.SettingMapper;
import com.martin.bookstore.entity.Setting;
import com.martin.bookstore.repository.SettingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SettingService {

    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;

    public SettingService(SettingRepository settingRepository, SettingMapper settingMapper) {
        this.settingRepository = settingRepository;
        this.settingMapper = settingMapper;
    }

    public List<SettingDto> getAllSettings() {
        return settingRepository.findAll().stream().map(settingMapper::toDto).collect(Collectors.toList());
    }

    public SettingDto getSettingById(Long id) {
        return settingRepository.findById(id).map(settingMapper::toDto).orElseThrow(() -> new RuntimeException("setting not found"));
    }

    public SettingDto createSetting(SettingDto settingDto) {
        Setting saved = settingRepository.save(settingMapper.toEntity(settingDto));
        return settingMapper.toDto(saved);
    }

    public SettingDto updateSetting(Long id, SettingDto updatedSettingDto) {
        Optional<Setting> optionalSetting = settingRepository.findById(id);
        if (optionalSetting.isPresent()) {
            Setting setting = optionalSetting.get();
            setting.setName(updatedSettingDto.getName());
            return settingMapper.toDto(settingRepository.save(setting));
        }
        return null;
    }

    public void deleteSetting(Long id) {
        settingRepository.deleteById(id);
    }
}
