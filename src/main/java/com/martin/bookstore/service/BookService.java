package com.martin.bookstore.service;

import com.martin.bookstore.exception.NotFoundException;
import com.martin.bookstore.criteria.BookSearchCriteria;
import com.martin.bookstore.dto.response.PageResponseDto;
import com.martin.bookstore.dto.request.BookRequestDto;
import com.martin.bookstore.dto.response.*;
import com.martin.bookstore.entity.*;
import com.martin.bookstore.mapper.*;
import com.martin.bookstore.repository.*;
import com.martin.bookstore.entity.Character;
import com.martin.bookstore.repository.CharacterRepository;
import com.martin.bookstore.repository.EditionRepository;
import com.martin.bookstore.repository.FormatRepository;
import com.martin.bookstore.entity.Genre;
import com.martin.bookstore.repository.GenreRepository;
import com.martin.bookstore.repository.LanguageRepository;
import com.martin.bookstore.repository.PublisherRepository;
import com.martin.bookstore.repository.SeriesRepository;
import com.martin.bookstore.entity.Setting;
import com.martin.bookstore.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
    private final GenreMapper genreMapper;
    private final AuthorMapper authorMapper;
    private final SettingMapper settingMapper;
    private final AwardMapper awardMapper;
    private final CharacterMapper characterMapper;

    public BookResponseDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::asOutput)
                .orElseThrow(() -> new NotFoundException("book with id " + id + " not found"));
    }

    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        Book book = bookMapper.asEntity(dto);
        Book savedBook = bookRepository.save(book);

        if (dto.getAuthorIds() != null) {
            dto.getAuthorIds().forEach(authorId -> {
                Author author = authorRepository.findById(authorId).orElseThrow(() -> new NotFoundException("Author with id " + authorId + " not found"));
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setBook(savedBook);
                bookAuthor.setAuthor(author);

                String contribution = dto.getAuthorContributions() != null
                        ? dto.getAuthorContributions().get(authorId)
                        : "Author";

                bookAuthor.setContribution(contribution);
                bookAuthorRepository.save(bookAuthor);
            });
        }

        if (dto.getGenreIds() != null) {
            dto.getGenreIds().forEach(genreId -> {
                Genre genre = genreRepository.findById(genreId)
                        .orElseThrow(() -> new NotFoundException("Genre with id " + genreId + " not found"));
                BookGenre bookGenre = new BookGenre();
                bookGenre.setBook(savedBook);
                bookGenre.setGenre(genre);
                bookGenreRepository.save(bookGenre);
            });
        }

        if (dto.getCharacterIds() != null) {
            dto.getCharacterIds().forEach(characterId -> {
                Character character = characterRepository.findById(characterId)
                        .orElseThrow(() -> new NotFoundException("Character with id " + characterId + " not found"));
                BookCharacter bc = new BookCharacter();
                bc.setBook(savedBook);
                bc.setCharacter(character);
                bookCharacterRepository.save(bc);
            });
        }

        if (dto.getSettingIds() != null) {
            dto.getSettingIds().forEach(settingId -> {
                Setting setting = settingRepository.findById(settingId)
                        .orElseThrow(() -> new NotFoundException("Setting with id " + settingId + " not found"));
                BookSetting bs = new BookSetting();
                bs.setBook(savedBook);
                bs.setSetting(setting);
                bookSettingRepository.save(bs);
            });
        }

        if (dto.getAwardIds() != null) {
            dto.getAwardIds().forEach(awardId -> {
                Award award = awardRepository.findById(awardId)
                        .orElseThrow(() -> new NotFoundException("Award with id " + awardId + " not found"));
                BookAward ba = new BookAward();
                ba.setBook(savedBook);
                ba.setAward(award);
                LocalDate year = LocalDate.now();
                if (dto.getAwardYears() != null && dto.getAwardYears().containsKey(awardId)) {
                    year = LocalDate.parse(dto.getAwardYears().get(awardId));
                }
                ba.setYear(year);
                bookAwardRepository.save(ba);
            });
        }

        return bookMapper.asOutput(savedBook);
    }


    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException("book with id " + id + " not found"));
        bookMapper.update(book, dto);

        if (dto.getEditionId() != null) {
            book.setEdition(editionRepository.findById(dto.getEditionId())
                    .orElseThrow(() -> new NotFoundException("Edition with id " + dto.getEditionId() + " not found")));
        }
        if (dto.getLanguageId() != null) {
            book.setLanguage(languageRepository.findById(dto.getLanguageId())
                    .orElseThrow(() -> new NotFoundException("language with id " + dto.getLanguageId() + " not found")));
        }
        if (dto.getPublisherId() != null) {
            book.setPublisher(publisherRepository.findById(dto.getPublisherId())
                    .orElseThrow(() -> new NotFoundException("Publisher with id " + dto.getPublisherId() + " not found")));
        }
        if (dto.getFormatId() != null) {
            book.setFormat(formatRepository.findById(dto.getFormatId())
                    .orElseThrow(() -> new NotFoundException("Format with id " + dto.getFormatId() + " not found")));
        }
        if (dto.getSeriesId() != null) {
            book.setSeries(seriesRepository.findById(dto.getSeriesId())
                    .orElseThrow(() -> new NotFoundException("Series with id " + dto.getSeriesId() + " not found")));
        }

        return bookMapper.asOutput(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));

        bookAuthorRepository.deleteAllByBook(book);
        bookAwardRepository.deleteAllByBook(book);
        bookCharacterRepository.deleteAllByBook(book);
        bookSettingRepository.deleteAllByBook(book);
        bookGenreRepository.deleteAllByBook(book);

        bookRepository.delete(book);
    }

    public PageResponseDto<BookResponseDto> getAll(BookSearchCriteria criteria) {
        Page<BookResponseDto> page = bookRepository.findAll(
                criteria,
                criteria.buildPageRequest()
        );
        return PageResponseDto.from(page);
    }

    public List<GenreResponseDto> getGenresByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));
        return book.getBookGenres().stream()
                .map(BookGenre::getGenre)
                .map(genreMapper::asOutput)
                .collect(Collectors.toList());
    }

    public List<AuthorResponseDto> getAuthorsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));
        return book.getBookAuthor().stream()
                .map(BookAuthor::getAuthor)
                .map(authorMapper::asOutput)
                .collect(Collectors.toList());
    }

    public List<SettingResponseDto> getSettingsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));
        return book.getBookSetting().stream()
                .map(BookSetting::getSetting)
                .map(settingMapper::asOutput)
                .collect(Collectors.toList());
    }

    public List<AwardResponseDto> getAwardsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));
        return book.getBookAwards().stream()
                .map(BookAward::getAward)
                .map(awardMapper::asOutput)
                .collect(Collectors.toList());
    }

    public List<CharacterResponseDto> getCharactersByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found: " + bookId));
        return book.getBookCharacters().stream()
                .map(BookCharacter::getCharacter)
                .map(characterMapper::asOutput)
                .collect(Collectors.toList());
    }

    public List<BookResponseDto> findSimilarBooks(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));

        List<Long> authorIds = book.getBookAuthor().stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getId())
                .collect(Collectors.toList());

        List<Long> genreIds = book.getBookGenres().stream()
                .map(bookGenre -> bookGenre.getGenre().getId())
                .collect(Collectors.toList());

        Long seriesId = book.getSeries() != null ? book.getSeries().getId() : null;

        List<Book> similarBooks = bookRepository.findSimilarBooks(
                bookId,
                authorIds.isEmpty() ? List.of(-1L) : authorIds,
                genreIds.isEmpty() ? List.of(-1L) : genreIds,
                seriesId,
                PageRequest.of(0, 10)
        );

        return similarBooks.stream()
                .map(bookMapper::asOutput)
                .collect(Collectors.toList());
    }


}
