package com.martin.bookstore.models.book.service;

import com.martin.bookstore.models.book.dto.BookDto;
import com.martin.bookstore.models.book.dto.BookMapper;
import com.martin.bookstore.models.book.persistence.entity.Book;
import com.martin.bookstore.models.book.persistence.repository.BookRepository;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final FormatRepository formatRepository;
    private final SeriesRepository seriesRepository;

    private final BookMapper bookMapper;




    public BookServiceImpl(BookRepository bookRepository, EditionRepository editionRepository,
                           LanguageRepository languageRepository, PublisherRepository publisherRepository,
                           FormatRepository formatRepository, SeriesRepository seriesRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.editionRepository = editionRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.formatRepository = formatRepository;
        this.seriesRepository = seriesRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow(() -> new RuntimeException("book not found"));
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found with id: " + id));

        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setPages(bookDto.getPages());
        book.setPublishDate(bookDto.getPublishDate());
        book.setFirstPublishDate(bookDto.getFirstPublishDate());
        book.setRating(bookDto.getRating());
        book.setLikedPercentage(bookDto.getLikedPercentage());
        book.setPrice(bookDto.getPrice());
        book.setNumRatings(bookDto.getNumRatings());
        book.setFiveStarRatings(bookDto.getFiveStarRatings());
        book.setFourStarRatings(bookDto.getFourStarRatings());
        book.setThreeStarRatings(bookDto.getThreeStarRatings());
        book.setTwoStarRatings(bookDto.getTwoStarRatings());
        book.setOneStarRatings(bookDto.getOneStarRatings());
        book.setBbeVotes(bookDto.getBbeVotes());
        book.setBbeScore(bookDto.getBbeScore());
        book.setCoverImageUrl(bookDto.getCoverImageUrl());

        if (bookDto.getEditionId() != null) {
            Edition edition = editionRepository.findById(bookDto.getEditionId()).orElseThrow();
            book.setEdition(edition);
        }

        if (bookDto.getLanguageId() != null) {
            Language language = languageRepository.findById(bookDto.getLanguageId()).orElseThrow();
            book.setLanguage(language);
        }

        if (bookDto.getPublisherId() != null) {
            Publisher publisher = publisherRepository.findById(bookDto.getPublisherId()).orElseThrow();
            book.setPublisher(publisher);
        }

        if (bookDto.getBookFormatId() != null) {
            Format format = formatRepository.findById(bookDto.getBookFormatId()).orElseThrow();
            book.setFormat(format);
        }

        if (bookDto.getSeriesId() != null) {
            Series series = seriesRepository.findById(bookDto.getSeriesId()).orElseThrow();
            book.setSeries(series);
        }

        // many to many

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
