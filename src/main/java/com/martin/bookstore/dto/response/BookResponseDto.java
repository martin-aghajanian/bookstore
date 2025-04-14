package com.martin.bookstore.dto.response;

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
}

