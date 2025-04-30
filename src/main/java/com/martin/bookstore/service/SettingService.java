package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.DeleteNotAllowedException;
import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.dto.request.SettingRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.dto.response.SettingResponseDto;
import com.martin.bookstore.entity.Setting;
import com.martin.bookstore.core.mapper.SettingMapper;
import com.martin.bookstore.repository.BookRepository;
import com.martin.bookstore.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;
    private final SettingMapper settingMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public List<SettingResponseDto> getAllSettings() {
        return settingMapper.asOutput(settingRepository.findAll());
    }

    public SettingResponseDto getSettingById(Long id) {
        return settingRepository.findById(id)
                .map(settingMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("setting with id " + id + " not found"));
    }

    public SettingResponseDto createSetting(SettingRequestDto dto) {
        Setting saved = settingRepository.save(settingMapper.asEntity(dto));
        return settingMapper.asOutput(saved);
    }

    public SettingResponseDto updateSetting(Long id, SettingRequestDto dto) {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("setting with id " + id + " not found"));
        settingMapper.update(setting, dto);
        return settingMapper.asOutput(settingRepository.save(setting));
    }

    public void deleteSetting(Long id) {
        Setting setting = settingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("setting with id " + id + " not found"));

        if (setting.getBookSetting() != null && !setting.getBookSetting().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete setting associated with books");
        }

        settingRepository.delete(setting);
    }

    public Page<BookResponseDto> getBooksBySetting(Long settingId, Pageable pageable) {
        settingRepository.findById(settingId)
                .orElseThrow(() -> new NotFoundException("setting with id " + settingId + " not found"));

        return bookRepository.findBySettingId(settingId, pageable)
                .map(bookMapper::asOutput);
    }
}
