package com.martin.bookstore.models.book.dto;

import com.martin.bookstore.models.book.persistence.entity.Book;
import com.martin.bookstore.models.edition.persistence.entity.Edition;
import com.martin.bookstore.models.edition.persistence.repository.EditionRepository;
import com.martin.bookstore.models.format.persistence.entity.Format;
import com.martin.bookstore.models.format.persistence.repository.FormatRepository;
import com.martin.bookstore.models.language.persistence.entity.Language;
import com.martin.bookstore.models.language.persistence.repository.LanguageRepository;
import com.martin.bookstore.models.publisher.persistence.entity.Publisher;
import com.martin.bookstore.models.publisher.persistence.repository.PublisherRepository;
import com.martin.bookstore.models.series.persistence.entity.Series;
import com.martin.bookstore.models.series.persistence.repository.SeriesRepository;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final FormatRepository formatRepository;

    public BookMapper(EditionRepository editionRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, SeriesRepository seriesRepository, FormatRepository formatRepository) {
        this.editionRepository = editionRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.seriesRepository = seriesRepository;
        this.formatRepository = formatRepository;
    }

    public BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        dto.setIsbn(book.getIsbn());
        dto.setPages(book.getPages());
        dto.setPublishDate(book.getPublishDate());
        dto.setFirstPublishDate(book.getFirstPublishDate());
        dto.setRating(book.getRating());
        dto.setLikedPercentage(book.getLikedPercentage());
        dto.setPrice(book.getPrice());
        dto.setNumRatings(book.getNumRatings());
        dto.setFiveStarRatings(book.getFiveStarRatings());
        dto.setFourStarRatings(book.getFourStarRatings());
        dto.setThreeStarRatings(book.getThreeStarRatings());
        dto.setTwoStarRatings(book.getTwoStarRatings());
        dto.setOneStarRatings(book.getOneStarRatings());
        dto.setBbeVotes(book.getBbeVotes());
        dto.setBbeScore(book.getBbeScore());
        dto.setCoverImageUrl(book.getCoverImageUrl());

        dto.setEditionId(book.getEdition() != null ? book.getEdition().getId() : null);
        dto.setSeriesId(book.getSeries() != null ? book.getSeries().getId() : null);
        dto.setLanguageId(book.getLanguage() != null ? book.getLanguage().getId() : null);
        dto.setPublisherId(book.getPublisher() != null ? book.getPublisher().getId() : null);
        dto.setBookFormatId(book.getFormat() != null ? book.getFormat().getId() : null);

        return dto;
    }

    public Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setDescription(dto.getDescription());
        book.setPages(dto.getPages());
        book.setPublishDate(dto.getPublishDate());
        book.setFirstPublishDate(dto.getFirstPublishDate());
        book.setRating(dto.getRating());
        book.setLikedPercentage(dto.getLikedPercentage());
        book.setPrice(dto.getPrice());
        book.setNumRatings(dto.getNumRatings());
        book.setFiveStarRatings(dto.getFiveStarRatings());
        book.setFourStarRatings(dto.getFourStarRatings());
        book.setThreeStarRatings(dto.getThreeStarRatings());
        book.setTwoStarRatings(dto.getTwoStarRatings());
        book.setOneStarRatings(dto.getOneStarRatings());
        book.setBbeVotes(dto.getBbeVotes());
        book.setBbeScore(dto.getBbeScore());
        book.setCoverImageUrl(dto.getCoverImageUrl());

        if (dto.getEditionId() != null) {
            Edition edition = editionRepository.findById(dto.getEditionId()).orElseThrow();
            book.setEdition(edition);
        }

        if (dto.getLanguageId() != null) {
            Language language = languageRepository.findById(dto.getLanguageId()).orElseThrow();
            book.setLanguage(language);
        }

        if (dto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(dto.getPublisherId()).orElseThrow();
            book.setPublisher(publisher);
        }

        if (dto.getSeriesId() != null) {
            Series series = seriesRepository.findById(dto.getSeriesId()).orElseThrow();
            book.setSeries(series);
        }

        if (dto.getBookFormatId() != null) {
            Format format = formatRepository.findById(dto.getBookFormatId()).orElseThrow();
            book.setFormat(format);
        }


        return book;
    }
}
