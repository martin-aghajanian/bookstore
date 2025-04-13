package com.martin.bookstore.core.mapper;

import com.martin.bookstore.dto.BookDto;
import com.martin.bookstore.entity.*;
import com.martin.bookstore.repository.AuthorRepository;
import com.martin.bookstore.repository.AwardRepository;
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
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final FormatRepository formatRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final CharacterRepository characterRepository;
    private final SettingRepository settingRepository;
    private final AwardRepository awardRepository;

    public BookMapper(EditionRepository editionRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, SeriesRepository seriesRepository, FormatRepository formatRepository, AuthorRepository authorRepository, GenreRepository genreRepository, CharacterRepository characterRepository, SettingRepository settingRepository, AwardRepository awardRepository) {
        this.editionRepository = editionRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.seriesRepository = seriesRepository;
        this.formatRepository = formatRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.characterRepository = characterRepository;
        this.settingRepository = settingRepository;
        this.awardRepository = awardRepository;
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

        if (book.getBookAuthor() != null) {
            dto.setAuthorIds(book.getBookAuthor().stream()
                    .map(bookAuthor -> bookAuthor.getAuthor().getId())
                    .collect(Collectors.toList()));

            dto.setAuthorContributions(book.getBookAuthor().stream()
                    .collect(Collectors.toMap(
                            ba -> ba.getAuthor().getId(),
                            BookAuthor::getContribution,
                            (existing, replacement) -> existing
                    )));

        }

        if (book.getBookGenres() != null) {
            dto.setGenreIds(book.getBookGenres().stream()
                    .map(bg -> bg.getGenre().getId())
                    .collect(Collectors.toList()));
        }

        if (book.getBookCharacters() != null) {
            dto.setCharacterIds(book.getBookCharacters().stream()
                    .map(bc -> bc.getCharacter().getId())
                    .collect(Collectors.toList()));
        }

        if (book.getBookSetting() != null) {
            dto.setSettingIds(book.getBookSetting().stream()
                    .map(bs -> bs.getSetting().getId())
                    .collect(Collectors.toList()));
        }

        if (book.getBookAwards() != null) {
            dto.setAwardIds(book.getBookAwards().stream()
                    .map(ba -> ba.getAward().getId())
                    .collect(Collectors.toList()));

            dto.setAwardYears(book.getBookAwards().stream()
                    .collect(Collectors.toMap(
                            ba -> ba.getAward().getId(),
                            ba -> ba.getYear().toString(),
                            (existing, replacement) -> existing
                    )));
        }

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

        if (dto.getAuthorIds() != null) {
            List<BookAuthor> authors = dto.getAuthorIds().stream().map(id -> {
                Author author = authorRepository.findById(id).orElseThrow();
                BookAuthor ba = new BookAuthor();
                ba.setAuthor(author);
                ba.setBook(book);

                String contribution = dto.getAuthorContributions() != null
                        ? dto.getAuthorContributions().get(id)
                        : "Author";
                ba.setContribution(contribution);

                return ba;
            }).collect(Collectors.toList());
            book.setBookAuthor(authors);
        }

        if (dto.getGenreIds() != null) {
            List<BookGenre> genres = dto.getGenreIds().stream().map(id -> {
                Genre genre = genreRepository.findById(id).orElseThrow();
                BookGenre bg = new BookGenre();
                bg.setGenre(genre);
                bg.setBook(book);
                return bg;
            }).collect(Collectors.toList());
            book.setBookGenres(genres);
        }

        if (dto.getCharacterIds() != null) {
            List<BookCharacter> characters = dto.getCharacterIds().stream().map(id -> {
                Character character = characterRepository.findById(id).orElseThrow();
                BookCharacter bc = new BookCharacter();
                bc.setCharacter(character);
                bc.setBook(book);
                return bc;
            }).collect(Collectors.toList());
            book.setBookCharacters(characters);
        }

        if (dto.getSettingIds() != null) {
            List<BookSetting> settings = dto.getSettingIds().stream().map(id -> {
                Setting setting = settingRepository.findById(id).orElseThrow();
                BookSetting bs = new BookSetting();
                bs.setSetting(setting);
                bs.setBook(book);
                return bs;
            }).collect(Collectors.toList());
            book.setBookSetting(settings);
        }

        if (dto.getAwardIds() != null) {
            List<BookAward> awards = dto.getAwardIds().stream().map(id -> {
                Award award = awardRepository.findById(id).orElseThrow();
                BookAward ba = new BookAward();
                ba.setAward(award);
                ba.setBook(book);

                LocalDate year = LocalDate.now();
                if (dto.getAwardYears() != null && dto.getAwardYears().containsKey(id)) {
                    year = LocalDate.parse(dto.getAwardYears().get(id));
                }
                ba.setYear(year);

                return ba;
            }).collect(Collectors.toList());
            book.setBookAwards(awards);
        }


        return book;
    }
}
