package com.martin.bookstore.models.setting.service;

import com.martin.bookstore.models.setting.dto.SettingDto;
import com.martin.bookstore.models.setting.dto.SettingMapper;
import com.martin.bookstore.models.setting.persistence.entity.Setting;
import com.martin.bookstore.models.setting.persistence.repository.SettingRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SettingServiceImpl implements SettingService{

    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;

    public SettingServiceImpl(SettingRepository settingRepository, SettingMapper settingMapper) {
        this.settingRepository = settingRepository;
        this.settingMapper = settingMapper;
    }

    @Override
    public List<SettingDto> getAllSettings() {
        return settingRepository.findAll().stream().map(settingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SettingDto getSettingById(Long id) {
        return settingRepository.findById(id).map(settingMapper::toDto).orElseThrow(() -> new RuntimeException("setting not found"));
    }

    @Override
    public SettingDto createSetting(SettingDto settingDto) {
        Setting saved = settingRepository.save(settingMapper.toEntity(settingDto));
        return settingMapper.toDto(saved);
    }

    @Override
    public SettingDto updateSetting(Long id, SettingDto updatedSettingDto) {
        Optional<Setting> optionalSetting = settingRepository.findById(id);
        if (optionalSetting.isPresent()) {
            Setting setting = optionalSetting.get();
            setting.setName(updatedSettingDto.getName());
            return settingMapper.toDto(settingRepository.save(setting));
        }
        return null;
    }

    @Override
    public void deleteSetting(Long id) {
        settingRepository.deleteById(id);
    }
}
