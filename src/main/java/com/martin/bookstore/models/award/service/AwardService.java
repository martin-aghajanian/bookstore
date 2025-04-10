package com.martin.bookstore.models.award.service;

import com.martin.bookstore.models.award.dto.AwardDto;

import java.util.List;

public interface AwardService {

    List<AwardDto> getAllAwards();

    AwardDto getAwardById(Long id);

    AwardDto createAward(AwardDto awardDto);

    AwardDto updateAward(Long id, AwardDto updatedAwardDto);

    void deleteAward(Long id);
}
