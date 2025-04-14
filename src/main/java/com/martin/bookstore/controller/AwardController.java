package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
import com.martin.bookstore.service.AwardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/awards")
@RequiredArgsConstructor
public class AwardController {

    private final AwardService awardService;

    @GetMapping("/{id}")
    public AwardResponseDto getAwardById(@PathVariable Long id) {
        return awardService.getAwardById(id);
    }

    @PostMapping
    public AwardResponseDto createAward(@RequestBody AwardRequestDto dto) {
        return awardService.createAward(dto);
    }

    @PutMapping("/{id}")
    public AwardResponseDto updateAward(@PathVariable Long id, @RequestBody AwardRequestDto dto) {
        return awardService.updateAward(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteAward(@PathVariable Long id) {
        awardService.deleteAward(id);
    }

    @GetMapping
    public Page<AwardResponseDto> filterByYear(@RequestParam(required = false) Integer year,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return awardService.filterByYear(year, PageRequest.of(page, size));
    }


    @GetMapping("/search")
    public Page<AwardResponseDto> searchAwards(@RequestParam String name,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        return awardService.searchByName(name, PageRequest.of(page, size));
    }
}
