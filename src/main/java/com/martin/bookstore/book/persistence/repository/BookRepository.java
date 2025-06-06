package com.martin.bookstore.book.persistence.repository;

import com.martin.bookstore.book.dto.BookResponseDto;
import com.martin.bookstore.book.criteria.BookSearchCriteria;
import com.martin.bookstore.book.persistence.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findByEditionId(Long editionId, Pageable pageable);

    Page<Book> findByFormatId(Long formatId, Pageable pageable);

    Page<Book> findByLanguageId(Long languageId, Pageable pageable);

    Page<Book> findBySeriesId(Long seriesId, Pageable pageable);

    @Query("""
        SELECT bs.book FROM BookSetting bs
        WHERE bs.setting.id = :settingId
    """)
    Page<Book> findBySettingId(@Param("settingId") Long settingId, Pageable pageable);

    @Query("""
        select distinct new com.martin.bookstore.book.dto.BookResponseDto(
            b.id,
            b.isbn,
            b.title,
            b.description,
            b.pages,
            b.publishDate,
            b.firstPublishDate,
            b.rating,
            b.likedPercentage,
            b.price,
            b.numRatings,
            b.fiveStarRatings,
            b.fourStarRatings,
            b.threeStarRatings,
            b.twoStarRatings,
            b.oneStarRatings,
            b.bbeVotes,
            b.bbeScore,
            new com.martin.bookstore.edition.dto.EditionResponseDto(e.id, e.name),
            new com.martin.bookstore.series.dto.SeriesResponseDto(s.id, s.name),
            new com.martin.bookstore.language.dto.LanguageResponseDto(l.id, l.name),
            new com.martin.bookstore.publisher.dto.PublisherResponseDto(p.id, p.name),
            new com.martin.bookstore.format.dto.FormatResponseDto(f.id, f.format)
        )
        from Book b
        left join b.edition e
        left join b.series s
        left join b.language l
        left join b.publisher p
        left join b.format f
        left join b.bookGenres bg
        left join b.bookAuthor ba
        where (:#{#criteria.query} is null
               or lower(b.title) like concat('%', lower(:#{#criteria.query}), '%')
               or lower(b.description) like concat('%', lower(:#{#criteria.query}), '%'))
          and (:#{#criteria.genreId} is null or bg.genre.id = :#{#criteria.genreId})
          and (:#{#criteria.authorId}    is null or ba.author.id = :#{#criteria.authorId})
          and (:#{#criteria.editionId} is null or e.id = :#{#criteria.editionId})
          and (:#{#criteria.seriesId} is null or s.id = :#{#criteria.seriesId})
          and (:#{#criteria.languageId} is null or l.id = :#{#criteria.languageId})
          and (:#{#criteria.publisherId} is null or p.id = :#{#criteria.publisherId})
          and (:#{#criteria.formatId} is null or f.id = :#{#criteria.formatId})
          and b.publishDate >= coalesce(:#{#criteria.minDate}, b.publishDate)
          and b.publishDate <= coalesce(:#{#criteria.maxDate}, b.publishDate)
          and b.pages >= coalesce(:#{#criteria.minPages}, b.pages)
          and b.pages <= coalesce(:#{#criteria.maxPages}, b.pages)
          and b.price >= coalesce(:#{#criteria.minPrice}, b.price)
          and b.price <= coalesce(:#{#criteria.maxPrice}, b.price)
    """)
    Page<BookResponseDto> findAll(BookSearchCriteria criteria, Pageable pageable);

    Page<Book> findByPublisherId(Long publisherId, PageRequest pageRequest);

    @Query("""
        select b
        from Book b
        where lower(b.title) in :titles
""")
    Page<Book> findByTitleInIgnoreCase(@Param("titles") Collection<String> lowerCaseTitles, Pageable pageable);

    @Query("""
    select distinct b from Book b
    left join BookAuthor ba on ba.book.id = b.id
    left join Author a on ba.author.id = a.id
    left join BookGenre bg on bg.book.id = b.id
    left join Genre g on bg.genre.id = g.id
    left join b.series s
    where b.id <> :bookId and (
        a.id in :authorIds or
        g.id in :genreIds or
        (:seriesId is not null and s.id = :seriesId)
    )
""")
    List<Book> findSimilarBooks(
            @Param("bookId") Long bookId,
            @Param("authorIds") List<Long> authorIds,
            @Param("genreIds") List<Long> genreIds,
            @Param("seriesId") Long seriesId,
            Pageable pageable
    );
}
