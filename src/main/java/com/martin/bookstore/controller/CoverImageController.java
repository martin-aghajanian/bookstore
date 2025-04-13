package com.martin.bookstore.controller;

import com.martin.bookstore.service.CoverImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cover-images")
@RequiredArgsConstructor
public class CoverImageController {

    private final CoverImageService coverImageService;

    @PostMapping("/download")
    public String downloadCovers() {
        coverImageService.processBookCovers();
        return "cover images downloaded/processed";
    }
}
