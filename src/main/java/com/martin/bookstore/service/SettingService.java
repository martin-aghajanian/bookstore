package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.SettingRequestDto;
import com.martin.bookstore.dto.response.SettingResponseDto;
import com.martin.bookstore.entity.Setting;
import com.martin.bookstore.core.mapper.SettingMapper;
import com.martin.bookstore.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;

    public List<SettingResponseDto> getAllSettings() {
        return settingMapper.asOutput(settingRepository.findAll());
    }

    public SettingResponseDto getSettingById(Long id) {
        return settingRepository.findById(id)
                .map(settingMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("setting not found"));
    }

    public SettingResponseDto createSetting(SettingRequestDto dto) {
        Setting saved = settingRepository.save(settingMapper.asEntity(dto));
        return settingMapper.asOutput(saved);
    }

    public SettingResponseDto updateSetting(Long id, SettingRequestDto dto) {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("setting not found"));
        settingMapper.update(setting, dto);
        return settingMapper.asOutput(settingRepository.save(setting));
    }

    public void deleteSetting(Long id) {
        settingRepository.deleteById(id);
    }
}
