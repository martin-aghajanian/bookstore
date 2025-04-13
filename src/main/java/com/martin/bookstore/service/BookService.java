package com.martin.bookstore.service;

import com.martin.bookstore.entity.*;
import com.martin.bookstore.repository.*;
import com.martin.bookstore.dto.BookDto;
import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.entity.Character;
import com.martin.bookstore.repository.CharacterRepository;
import com.martin.bookstore.entity.Edition;
import com.martin.bookstore.repository.EditionRepository;
import com.martin.bookstore.entity.Format;
import com.martin.bookstore.repository.FormatRepository;
import com.martin.bookstore.entity.Genre;
import com.martin.bookstore.repository.GenreRepository;
import com.martin.bookstore.entity.Language;
import com.martin.bookstore.repository.LanguageRepository;
import com.martin.bookstore.entity.Publisher;
import com.martin.bookstore.repository.PublisherRepository;
import com.martin.bookstore.entity.Series;
import com.martin.bookstore.repository.SeriesRepository;
import com.martin.bookstore.entity.Setting;
import com.martin.bookstore.repository.SettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final FormatRepository formatRepository;
    private final SeriesRepository seriesRepository;

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CharacterRepository characterRepository;
    private final SettingRepository settingRepository;
    private final AwardRepository awardRepository;

    private final BookAuthorRepository bookAuthorRepository;
    private final BookGenreRepository bookGenreRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final BookSettingRepository bookSettingRepository;
    private final BookAwardRepository bookAwardRepository;


    private final BookMapper bookMapper;




    public BookService(BookRepository bookRepository, EditionRepository editionRepository,
                       LanguageRepository languageRepository, PublisherRepository publisherRepository,
                       FormatRepository formatRepository, SeriesRepository seriesRepository, AuthorRepository authorRepository, GenreRepository genreRepository, CharacterRepository characterRepository, SettingRepository settingRepository, AwardRepository awardRepository, BookAuthorRepository bookAuthorRepository, BookGenreRepository bookGenreRepository, BookCharacterRepository bookCharacterRepository, BookSettingRepository bookSettingRepository, BookAwardRepository bookAwardRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.editionRepository = editionRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.formatRepository = formatRepository;
        this.seriesRepository = seriesRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.characterRepository = characterRepository;
        this.settingRepository = settingRepository;
        this.awardRepository = awardRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookGenreRepository = bookGenreRepository;
        this.bookCharacterRepository = bookCharacterRepository;
        this.bookSettingRepository = bookSettingRepository;
        this.bookAwardRepository = bookAwardRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    public BookDto getBookById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toDto).orElseThrow(() -> new RuntimeException("book not found"));
    }

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        Book savedBook = bookRepository.save(book);

        if (bookDto.getAuthorIds() != null) {
            for (Long authorId : bookDto.getAuthorIds()) {
                Author author = authorRepository.findById(authorId).orElseThrow();
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setBook(savedBook);
                bookAuthor.setAuthor(author);

                String contribution = bookDto.getAuthorContributions() != null
                        ? bookDto.getAuthorContributions().get(authorId)
                        : "Author";
                bookAuthor.setContribution(contribution);

                bookAuthorRepository.save(bookAuthor);
            }
        }

        if (bookDto.getGenreIds() != null) {
            for (Long genreId : bookDto.getGenreIds()) {
                Genre genre = genreRepository.findById(genreId).orElseThrow();
                BookGenre bookGenre = new BookGenre();
                bookGenre.setBook(savedBook);
                bookGenre.setGenre(genre);
                bookGenreRepository.save(bookGenre);
            }
        }

        if (bookDto.getCharacterIds() != null) {
            for (Long characterId : bookDto.getCharacterIds()) {
                Character character = characterRepository.findById(characterId).orElseThrow();
                BookCharacter bookCharacter = new BookCharacter();
                bookCharacter.setBook(savedBook);
                bookCharacter.setCharacter(character);
                bookCharacterRepository.save(bookCharacter);
            }
        }

        if (bookDto.getSettingIds() != null) {
            for (Long settingId : bookDto.getSettingIds()) {
                Setting setting = settingRepository.findById(settingId).orElseThrow();
                BookSetting bookSetting = new BookSetting();
                bookSetting.setBook(savedBook);
                bookSetting.setSetting(setting);
                bookSettingRepository.save(bookSetting);
            }
        }

        if (bookDto.getAwardIds() != null) {
            for (Long awardId : bookDto.getAwardIds()) {
                Award award = awardRepository.findById(awardId).orElseThrow();
                BookAward bookAward = new BookAward();
                bookAward.setBook(savedBook);
                bookAward.setAward(award);

                LocalDate year = LocalDate.now();
                if (bookDto.getAwardYears() != null && bookDto.getAwardYears().containsKey(awardId)) {
                    year = LocalDate.parse(bookDto.getAwardYears().get(awardId));
                }
                bookAward.setYear(year);

                bookAwardRepository.save(bookAward);
            }
        }

        return bookMapper.toDto(savedBook);
    }


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

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
