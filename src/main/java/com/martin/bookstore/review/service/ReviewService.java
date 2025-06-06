package com.martin.bookstore.review.service;

import com.martin.bookstore.review.persistence.repository.ReviewRepository;
import com.martin.bookstore.review.dto.ReviewRequestDto;
import com.martin.bookstore.review.dto.ReviewResponseDto;
import com.martin.bookstore.review.persistence.entity.Review;
import com.martin.bookstore.user.persistence.entity.User;
import com.martin.bookstore.shared.exception.NotFoundException;
import com.martin.bookstore.book.persistence.entity.Book;
import com.martin.bookstore.book.persistence.repository.BookRepository;
import com.martin.bookstore.user.persistence.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponseDto addReview(Long bookId, ReviewRequestDto dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));

        Review review = reviewMapper.asEntity(dto);
        review.setBook(book);
        review.setCreatedAt(LocalDateTime.now());
        review.setUser(user);

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
