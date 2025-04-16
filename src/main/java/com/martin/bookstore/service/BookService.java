package com.martin.bookstore.service;

import com.martin.bookstore.core.mapper.BookMapper;
import com.martin.bookstore.dto.request.BookRequestDto;
import com.martin.bookstore.dto.response.BookResponseDto;
import com.martin.bookstore.entity.*;
import com.martin.bookstore.repository.*;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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


    public List<BookResponseDto> getAllBooks() {
        return bookMapper.asOutput(bookRepository.findAll());
    }

    public BookResponseDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::asOutput)
                .orElseThrow(() -> new RuntimeException("book not found"));
    }

    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        Book book = bookMapper.asEntity(dto);
        Book savedBook = bookRepository.save(book);

        if (dto.getAuthorIds() != null) {
            dto.getAuthorIds().forEach(authorId -> {
                Author author = authorRepository.findById(authorId).orElseThrow();
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
                Genre genre = genreRepository.findById(genreId).orElseThrow();
                BookGenre bookGenre = new BookGenre();
                bookGenre.setBook(savedBook);
                bookGenre.setGenre(genre);
                bookGenreRepository.save(bookGenre);
            });
        }

        if (dto.getCharacterIds() != null) {
            dto.getCharacterIds().forEach(characterId -> {
                Character character = characterRepository.findById(characterId).orElseThrow();
                BookCharacter bc = new BookCharacter();
                bc.setBook(savedBook);
                bc.setCharacter(character);
                bookCharacterRepository.save(bc);
            });
        }

        if (dto.getSettingIds() != null) {
            dto.getSettingIds().forEach(settingId -> {
                Setting setting = settingRepository.findById(settingId).orElseThrow();
                BookSetting bs = new BookSetting();
                bs.setBook(savedBook);
                bs.setSetting(setting);
                bookSettingRepository.save(bs);
            });
        }

        if (dto.getAwardIds() != null) {
            dto.getAwardIds().forEach(awardId -> {
                Award award = awardRepository.findById(awardId).orElseThrow();
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
        Book book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("book not found"));
        bookMapper.update(book, dto);

        if (dto.getEditionId() != null) {
            book.setEdition(editionRepository.findById(dto.getEditionId()).orElseThrow());
        }
        if (dto.getLanguageId() != null) {
            book.setLanguage(languageRepository.findById(dto.getLanguageId()).orElseThrow());
        }
        if (dto.getPublisherId() != null) {
            book.setPublisher(publisherRepository.findById(dto.getPublisherId()).orElseThrow());
        }
        if (dto.getFormatId() != null) {
            book.setFormat(formatRepository.findById(dto.getFormatId()).orElseThrow());
        }
        if (dto.getSeriesId() != null) {
            book.setSeries(seriesRepository.findById(dto.getSeriesId()).orElseThrow());
        }

        return bookMapper.asOutput(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
