package com.martin.bookstore.repository.specification;

import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.BookGenre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> filter(
            Long genreId, Long languageId, Long formatId,
            Integer minPages, Integer maxPages,
            Double minPrice, Double maxPrice,
            LocalDate minDate, LocalDate maxDate
    ) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (genreId != null) {
                Join<Book, BookGenre> genreJoin = root.join("bookGenres", JoinType.LEFT);
                predicates.add(cb.equal(genreJoin.get("genre").get("id"), genreId));
            }

            if (languageId != null) {
                predicates.add(cb.equal(root.get("language").get("id"), languageId));
            }

            if (formatId != null) {
                predicates.add(cb.equal(root.get("format").get("id"), formatId));
            }

            if (minPages != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("pages"), minPages));
            }

            if (maxPages != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pages"), maxPages));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (minDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("publishDate"), minDate));
            }

            if (maxDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("publishDate"), maxDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

