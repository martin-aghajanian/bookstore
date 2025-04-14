package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.AwardRequestDto;
import com.martin.bookstore.dto.response.AwardResponseDto;
import com.martin.bookstore.service.AwardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/awards")
public class AwardController {

    private final AwardService awardService;

    public AwardController(AwardService awardService) {
        this.awardService = awardService;
    }

    @GetMapping
    public List<AwardResponseDto> getAllAwards() {
        return awardService.getAllAwards();
    }

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
}
