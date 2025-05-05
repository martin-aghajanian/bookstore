package com.martin.bookstore.controller;

import com.martin.bookstore.criteria.AwardSearchCriteria;
import com.martin.bookstore.dto.PageResponseDto;
import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AwardResponseDto getAwardById(@PathVariable Long id) {
        return awardService.getAwardById(id);
    }

    @PreAuthorize("hasAnyAuthority('content:create','data:create','admin:create')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AwardResponseDto createAward(@RequestBody AwardRequestDto dto) {
        return awardService.createAward(dto);
    }

    @PreAuthorize("hasAnyAuthority('content:update','data:update','admin:update')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AwardResponseDto updateAward(@PathVariable Long id, @RequestBody AwardRequestDto dto) {
        return awardService.updateAward(id, dto);
    }

    @PreAuthorize("hasAnyAuthority('content:delete','data:delete','admin:delete')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAward(@PathVariable Long id) {
        awardService.deleteAward(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto<AwardResponseDto> getAll(AwardSearchCriteria criteria) {
        return awardService.getAll(criteria);
    }

    @PreAuthorize("permitAll()")
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
