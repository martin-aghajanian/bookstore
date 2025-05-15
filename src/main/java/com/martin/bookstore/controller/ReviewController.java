package com.martin.bookstore.controller;

import com.martin.bookstore.dto.request.ReviewRequestDto;
import com.martin.bookstore.dto.response.ReviewResponseDto;
import com.martin.bookstore.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books/{bookId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto addReview(
            @PathVariable Long bookId,
            @RequestBody ReviewRequestDto dto) {
        return reviewService.addReview(bookId, dto);
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> listReviews(@PathVariable Long bookId) {
        return reviewService.getReviewsForBook(bookId);
    }
}
