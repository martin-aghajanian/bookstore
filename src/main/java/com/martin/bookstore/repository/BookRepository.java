package com.martin.bookstore.repository;

import com.martin.bookstore.criteria.BookSearchCriteria;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

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
        select distinct new com.martin.bookstore.dto.response.BookResponseDto(
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
            new com.martin.bookstore.dto.response.EditionResponseDto(e.id, e.name),
            new com.martin.bookstore.dto.response.SeriesResponseDto(s.id, s.name),
            new com.martin.bookstore.dto.response.LanguageResponseDto(l.id, l.name),
            new com.martin.bookstore.dto.response.PublisherResponseDto(p.id, p.name),
            new com.martin.bookstore.dto.response.FormatResponseDto(f.id, f.format)
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
}
