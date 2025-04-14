package com.martin.bookstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequestDto {
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

    private Long editionId;
    private Long seriesId;
    private Long languageId;
    private Long publisherId;
    private Long formatId;

    private List<Long> authorIds;
    private Map<Long, String> authorContributions;
    private List<Long> genreIds;
    private List<Long> characterIds;
    private List<Long> settingIds;
    private List<Long> awardIds;
    private Map<Long, String> awardYears;
}
