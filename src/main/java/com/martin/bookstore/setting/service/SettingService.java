package com.martin.bookstore.setting.service;

import com.martin.bookstore.setting.persistence.repository.SettingRepository;
import com.martin.bookstore.setting.dto.SettingRequestDto;
import com.martin.bookstore.setting.dto.SettingResponseDto;
import com.martin.bookstore.setting.persistence.entity.Setting;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.repository.BookRepository;
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
