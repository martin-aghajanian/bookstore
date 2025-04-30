package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
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
    public Page<AwardResponseDto> filterByYear(@RequestParam(required = false) Integer year,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return awardService.filterByYear(year, PageRequest.of(page, size));
    }


    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<AwardResponseDto> searchAwards(@RequestParam String name,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return awardService.searchByName(name, PageRequest.of(page, size));
    }
}
