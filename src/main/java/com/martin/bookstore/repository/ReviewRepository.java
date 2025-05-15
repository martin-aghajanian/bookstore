package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("""
select r
from Review r
where r.book.id = :bookId
order by r.createdAt desc
""")
    List<Review> findByBookIdOrderByCreatedAtDesc(@Param("bookId") Long bookId);
}
