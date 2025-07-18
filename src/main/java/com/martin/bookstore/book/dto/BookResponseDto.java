package com.martin.bookstore.book.dto;

import com.martin.bookstore.author.dto.AuthorResponseDto;
import com.martin.bookstore.award.dto.AwardResponseDto;
import com.martin.bookstore.character.dto.CharacterResponseDto;
import com.martin.bookstore.edition.dto.EditionResponseDto;
import com.martin.bookstore.format.dto.FormatResponseDto;
import com.martin.bookstore.genre.dto.GenreResponseDto;
import com.martin.bookstore.language.dto.LanguageResponseDto;
import com.martin.bookstore.publisher.dto.PublisherResponseDto;
import com.martin.bookstore.series.dto.SeriesResponseDto;
import com.martin.bookstore.setting.dto.SettingResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private Long isbn;
    private String title;
    private String description;
    private Integer pages;
    private LocalDate publishDate;
    private LocalDate firstPublishDate;
    private Double rating;
    private Double likedPercentage;
    private Double price;
    private Long numRatings;
    private Long fiveStarRatings;
    private Long fourStarRatings;
    private Long threeStarRatings;
    private Long twoStarRatings;
    private Long oneStarRatings;
    private Long bbeVotes;
    private Long bbeScore;
    private String coverImageUrl;

    private EditionResponseDto edition;
    private SeriesResponseDto series;
    private LanguageResponseDto language;
    private PublisherResponseDto publisher;
    private FormatResponseDto format;

    private List<AuthorResponseDto> authors;
    private List<GenreResponseDto> genres;
    private List<CharacterResponseDto> characters;
    private List<SettingResponseDto> settings;
    private List<AwardResponseDto> awards;

    public BookResponseDto(
            Long id,
            Long isbn,
            String title,
            String description,
            Integer pages,
            LocalDate publishDate,
            LocalDate firstPublishDate,
            Double rating,
            Double likedPercentage,
            Double price,
            Long numRatings,
            Long fiveStarRatings,
            Long fourStarRatings,
            Long threeStarRatings,
            Long twoStarRatings,
            Long oneStarRatings,
            Long bbeVotes,
            Long bbeScore,
            EditionResponseDto edition,
            SeriesResponseDto series,
            LanguageResponseDto language,
            PublisherResponseDto publisher,
            FormatResponseDto format
    ) {
        this.id                = id;
        this.isbn              = isbn;
        this.title             = title;
        this.description       = description;
        this.pages             = pages;
        this.publishDate       = publishDate;
        this.firstPublishDate  = firstPublishDate;
        this.rating            = rating;
        this.likedPercentage   = likedPercentage;
        this.price             = price;
        this.numRatings        = numRatings;
        this.fiveStarRatings   = fiveStarRatings;
        this.fourStarRatings   = fourStarRatings;
        this.threeStarRatings  = threeStarRatings;
        this.twoStarRatings    = twoStarRatings;
        this.oneStarRatings    = oneStarRatings;
        this.bbeVotes          = bbeVotes;
        this.bbeScore          = bbeScore;
        this.edition           = edition;
        this.series            = series;
        this.language          = language;
        this.publisher         = publisher;
        this.format            = format;
    }
}

