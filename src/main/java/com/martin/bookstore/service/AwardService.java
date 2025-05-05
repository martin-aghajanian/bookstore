package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.DeleteNotAllowedException;
import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.criteria.AwardSearchCriteria;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
import com.martin.bookstore.entity.Award;
import com.martin.bookstore.core.mapper.AwardMapper;
import com.martin.bookstore.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;

    public AwardResponseDto getAwardById(Long id) {
        return awardRepository.findById(id)
                .map(awardMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("Award with id " + id + " not found"));
    }

    public AwardResponseDto createAward(AwardRequestDto dto) {
        Award saved = awardRepository.save(awardMapper.asEntity(dto));
        return awardMapper.asOutput(saved);
    }

    public AwardResponseDto updateAward(Long id, AwardRequestDto dto) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Award with id " + id + " not found"));
        awardMapper.update(award, dto);
        return awardMapper.asOutput(awardRepository.save(award));
    }

    public void deleteAward(Long id) {
        Award award = awardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Award with id " + id + " not found"));

        if (!award.getBookAwards().isEmpty()) {
            throw new DeleteNotAllowedException("Cannot delete award: it is associated with books.");
        }

        awardRepository.delete(award);
    }

    public PageResponseDto<AwardResponseDto> getAll(AwardSearchCriteria criteria) {
        Page<AwardResponseDto> page = awardRepository.findAll(
                criteria,
                criteria.buildPageRequest()
        );
        return PageResponseDto.from(page);
    }

}
