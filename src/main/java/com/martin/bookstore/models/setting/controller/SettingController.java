package com.martin.bookstore.models.setting.controller;

import com.martin.bookstore.models.setting.dto.SettingDto;
import com.martin.bookstore.models.setting.service.SettingService;
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
    public List<SettingDto> getAllSettings() {
        return settingService.getAllSettings();
    }

    @GetMapping("/{id}")
    public SettingDto getSettingById(@PathVariable Long id) {
        return settingService.getSettingById(id);
    }

    @PostMapping
    public SettingDto createSetting(@RequestBody SettingDto settingDto) {
        return settingService.createSetting(settingDto);
    }

    @PutMapping("/{id}")
    public SettingDto updateSetting(@PathVariable Long id, @RequestBody SettingDto settingDto) {
        return settingService.updateSetting(id, settingDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSetting(@PathVariable Long id) {
        settingService.deleteSetting(id);
    }

}
