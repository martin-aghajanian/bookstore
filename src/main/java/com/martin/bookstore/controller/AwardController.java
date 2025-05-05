package com.martin.bookstore.controller;

import com.martin.bookstore.criteria.AwardSearchCriteria;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AwardResponseDto getAwardById(@PathVariable Long id) {
        return awardService.getAwardById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AwardResponseDto createAward(@RequestBody AwardRequestDto dto) {
        return awardService.createAward(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AwardResponseDto updateAward(@PathVariable Long id, @RequestBody AwardRequestDto dto) {
        return awardService.updateAward(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAward(@PathVariable Long id) {
        awardService.deleteAward(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AwardResponseDto> getAll(AwardSearchCriteria criteria) {
        return awardService.getAll(criteria);
    }

    @GetMapping("/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<BookResponseDto> getBooksByAward(
            @PathVariable("id") Long id,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return awardService.getBooksByAwardId(id, page, size);
    }
}
