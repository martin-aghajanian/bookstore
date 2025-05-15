package com.martin.bookstore.service;

import com.martin.bookstore.core.exception.NotFoundException;
import com.martin.bookstore.core.mapper.ReviewMapper;
import com.martin.bookstore.dto.request.ReviewRequestDto;
import com.martin.bookstore.dto.response.ReviewResponseDto;
import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.Review;
import com.martin.bookstore.repository.BookRepository;
import com.martin.bookstore.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponseDto addReview(Long bookId, ReviewRequestDto dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        Review review = reviewMapper.asEntity(dto);
        review.setBook(book);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        updateBookMetrics(book, dto.getRating());
        bookRepository.save(book);

        return reviewMapper.asDto(review);
    }

    private void updateBookMetrics(Book book, int newRating) {
        long total = book.getNumRatings() == null ? 0 : book.getNumRatings();
        long sumPrev = book.getRating() == null ? 0L : Math.round(book.getRating() * total);

        total++;
        book.setNumRatings(total);

        switch (newRating) {
            case 5 -> book.setFiveStarRatings(book.getFiveStarRatings() + 1);
            case 4 -> book.setFourStarRatings(book.getFourStarRatings() + 1);
            case 3 -> book.setThreeStarRatings(book.getThreeStarRatings() + 1);
            case 2 -> book.setTwoStarRatings(book.getTwoStarRatings() + 1);
            case 1 -> book.setOneStarRatings(book.getOneStarRatings() + 1);
        }

        double avg = (sumPrev + newRating) / (double) total;
        book.setRating(avg);

        long likes = book.getFiveStarRatings() + book.getFourStarRatings();
        book.setLikedPercentage((likes * 100.0) / total);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsForBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NotFoundException("Book not found: " + bookId);
        }
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId).stream()
                .map(reviewMapper::asDto)
                .collect(Collectors.toList());
    }
}
