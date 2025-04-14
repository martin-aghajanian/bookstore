package com.martin.bookstore.service;

import com.martin.bookstore.dto.old.AwardDto;
import com.martin.bookstore.core.mapper.old.AwardMapper;
import com.martin.bookstore.entity.Award;
import com.martin.bookstore.repository.AwardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    public AwardService(AwardRepository awardRepository, AwardMapper awardMapper) {
        this.awardRepository = awardRepository;
        this.awardMapper = awardMapper;
    }

    public List<AwardDto> getAllAwards() {
        return awardRepository.findAll().stream().map(awardMapper::toDto).collect(Collectors.toList());
    }

    public AwardDto getAwardById(Long id) {
        return awardRepository.findById(id).map(awardMapper::toDto).orElseThrow(() -> new RuntimeException("award not found"));
    }

    public AwardDto createAward(AwardDto awardDto) {
        Award saved = awardRepository.save(awardMapper.toEntity(awardDto));
        return awardMapper.toDto(saved);
    }

    public AwardDto updateAward(Long id, AwardDto updatedAwardDto) {
        Optional<Award> optionalAward = awardRepository.findById(id);
        if (optionalAward.isPresent()) {
            Award award = optionalAward.get();
            award.setName(updatedAwardDto.getName());
            return awardMapper.toDto(awardRepository.save(award));
        }
        return null;
    }

    public void deleteAward(Long id) {
        awardRepository.deleteById(id);
    }
}
