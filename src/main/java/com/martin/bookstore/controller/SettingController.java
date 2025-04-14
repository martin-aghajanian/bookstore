package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.SettingRequestDto;
import com.martin.bookstore.dto.response.SettingResponseDto;
import com.martin.bookstore.service.SettingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/settings")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping
    public List<SettingResponseDto> getAllSettings() {
        return settingService.getAllSettings();
    }

    @GetMapping("/{id}")
    public SettingResponseDto getSettingById(@PathVariable Long id) {
        return settingService.getSettingById(id);
    }

    @PostMapping
    public SettingResponseDto createSetting(@RequestBody SettingRequestDto dto) {
        return settingService.createSetting(dto);
    }

    @PutMapping("/{id}")
    public SettingResponseDto updateSetting(@PathVariable Long id, @RequestBody SettingRequestDto dto) {
        return settingService.updateSetting(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteSetting(@PathVariable Long id) {
        settingService.deleteSetting(id);
    }
}
