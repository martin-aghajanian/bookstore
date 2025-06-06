package com.martin.bookstore.award.service;

import com.martin.bookstore.award.criteria.AwardSearchCriteria;
import com.martin.bookstore.award.dto.AwardRequestDto;
import com.martin.bookstore.award.dto.AwardResponseDto;
import com.martin.bookstore.award.persistence.entity.Award;
import com.martin.bookstore.award.persistence.repository.AwardRepository;
import com.martin.bookstore.shared.exception.DeleteNotAllowedException;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.service.BookMapper;
import com.martin.bookstore.shared.dto.PageResponseDto;
import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.repository.BookAwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AwardService {

    private final AwardRepository awardRepository;
    private final AwardMapper awardMapper;
    private final BookAwardRepository bookAwardRepository;
    private final BookMapper bookMapper;

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

    public PageResponseDto<BookResponseDto> getBooksByAwardId(Long awardId, int page, int size) {
        awardRepository.findById(awardId)
                .orElseThrow(() -> new NotFoundException("Award with id " + awardId + " not found"));

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Book> bookPage = bookAwardRepository.findBooksByAwardId(awardId, pageRequest);
        Page<BookResponseDto> dtoPage = bookPage.map(bookMapper::asOutput);

        return PageResponseDto.from(dtoPage);
    }

}
