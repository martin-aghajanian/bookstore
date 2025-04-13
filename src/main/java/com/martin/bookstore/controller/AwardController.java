package com.martin.bookstore.controller;

import com.martin.bookstore.dto.AwardDto;
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
    public List<AwardDto> getAllAwards() {
        return awardService.getAllAwards();
    }

    @GetMapping("/{id}")
    public AwardDto getAwardById(@PathVariable Long id) {
        return awardService.getAwardById(id);
    }

    @PostMapping
    public AwardDto createAward(@RequestBody AwardDto awardDto) {
        return awardService.createAward(awardDto);
    }

    @PutMapping("/{id}")
    public AwardDto updateAward(@PathVariable Long id, @RequestBody AwardDto awardDto) {
        return awardService.updateAward(id, awardDto);
    }

    @DeleteMapping("/{id}")
    public void deleteAward(@PathVariable Long id) {
        awardService.deleteAward(id);
    }

}
