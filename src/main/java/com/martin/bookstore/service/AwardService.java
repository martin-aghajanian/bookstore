package com.martin.bookstore.service;

import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
import com.martin.bookstore.entity.Award;
import com.martin.bookstore.core.mapper.AwardMapper;
import com.martin.bookstore.repository.AwardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    public AwardService(AwardRepository awardRepository, AwardMapper awardMapper) {
        this.awardRepository = awardRepository;
        this.awardMapper = awardMapper;
    }

    public List<AwardResponseDto> getAllAwards() {
        return awardMapper.asOutput(awardRepository.findAll());
    }

    public AwardResponseDto getAwardById(Long id) {
        return awardRepository.findById(id)
                .map(awardMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("Award not found"));
    }

    public AwardResponseDto createAward(AwardRequestDto dto) {
        Award saved = awardRepository.save(awardMapper.asEntity(dto));
        return awardMapper.asOutput(saved);
    }

    public AwardResponseDto updateAward(Long id, AwardRequestDto dto) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Award not found"));
        awardMapper.update(award, dto);
        return awardMapper.asOutput(awardRepository.save(award));
    }

    public void deleteAward(Long id) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Award not found"));

        if (!award.getBookAwards().isEmpty()) {
            throw new IllegalStateException("Cannot delete award: it is associated with books.");
        }

        awardRepository.delete(award);
    }

    public Page<AwardResponseDto> filterByYear(Integer year, Pageable pageable) {
        if (year == null) {
            return awardRepository.findAll(pageable).map(awardMapper::asOutput);
        }
        return awardRepository.findAwardsByYear(year, pageable)
                .map(awardMapper::asOutput);
    }


    public Page<AwardResponseDto> searchByName(String name, Pageable pageable) {
        return awardRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(awardMapper::asOutput);
    }

}
