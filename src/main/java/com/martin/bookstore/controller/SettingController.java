package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.SettingRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.SettingResponseDto;
import com.martin.bookstore.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SettingResponseDto> getAllSettings() {
        return settingService.getAllSettings();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SettingResponseDto getSettingById(@PathVariable Long id) {
        return settingService.getSettingById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SettingResponseDto createSetting(@RequestBody SettingRequestDto dto) {
        return settingService.createSetting(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SettingResponseDto updateSetting(@PathVariable Long id, @RequestBody SettingRequestDto dto) {
        return settingService.updateSetting(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSetting(@PathVariable Long id) {
        settingService.deleteSetting(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public Page<BookResponseDto> getBooksBySetting(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return settingService.getBooksBySetting(id, pageable);
    }
}
