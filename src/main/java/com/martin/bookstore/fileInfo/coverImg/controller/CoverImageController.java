package com.martin.bookstore.fileInfo.coverImg.controller;

import com.martin.bookstore.fileInfo.coverImg.service.CoverImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cover-images")
@RequiredArgsConstructor
public class CoverImageController {

    private final CoverImageService coverImageService;

    @PreAuthorize("hasAuthority('data:import')")
    @PostMapping("/download")
    @ResponseStatus(HttpStatus.OK)
    public String downloadCovers() {
        coverImageService.processBookCovers();
        return "cover images downloaded/processed";
    }
}
