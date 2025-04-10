package com.martin.bookstore.models.award.service;

import com.martin.bookstore.models.award.dto.AwardDto;
import com.martin.bookstore.models.award.dto.AwardMapper;
import com.martin.bookstore.models.award.persistence.entity.Award;
import com.martin.bookstore.models.award.persistence.repository.AwardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AwardServiceImpl implements AwardService{

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    public AwardServiceImpl(AwardRepository awardRepository, AwardMapper awardMapper) {
        this.awardRepository = awardRepository;
        this.awardMapper = awardMapper;
    }

    @Override
    public List<AwardDto> getAllAwards() {
        return awardRepository.findAll().stream().map(awardMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public AwardDto getAwardById(Long id) {
        return awardRepository.findById(id).map(awardMapper::toDto).orElseThrow(() -> new RuntimeException("award not found"));
    }

    @Override
    public AwardDto createAward(AwardDto awardDto) {
        Award saved = awardRepository.save(awardMapper.toEntity(awardDto));
        return awardMapper.toDto(saved);
    }

    @Override
    public AwardDto updateAward(Long id, AwardDto updatedAwardDto) {
        Optional<Award> optionalAward = awardRepository.findById(id);
        if (optionalAward.isPresent()) {
            Award award = optionalAward.get();
            award.setName(updatedAwardDto.getName());
            return awardMapper.toDto(awardRepository.save(award));
        }
        return null;
    }

    @Override
    public void deleteAward(Long id) {
        awardRepository.deleteById(id);
    }
}
