package com.martin.bookstore.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@RequiredArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "isbn", unique = true, nullable = false)
    private Long isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "published_date", nullable = false)
    private LocalDate publishDate;

    @Column(name = "first_publish_date")
    private LocalDate firstPublishDate;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "liked_percentage")
    private Double likedPercentage;

    @Column(name = "price")
    private Double price;

    @Column(name = "series_order")
    private Integer seriesOrder;

    @Column(name = "num_ratings")
    private Long numRatings;

    @Column(name = "five_star_ratings")
    private Long fiveStarRatings;

    @Column(name = "four_star_ratings")
    private Long fourStarRatings;

    @Column(name = "three_star_ratings")
    private Long threeStarRatings;

    @Column(name = "two_star_ratings")
    private Long twoStarRatings;

    @Column(name = "one_star_ratings")
    private Long oneStarRatings;

    @Column(name = "num_bbe_votes")
    private Long bbeVotes;

    @Column(name = "bbe_score")
    private Long bbeScore;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    // many to one

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "edition_id")
    private Edition edition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_format_id")
    private BookFormat bookFormat;

    // many to many

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> bookAuthor;

    @OneToMany(mappedBy = "book")
    private List<BookGenre> bookGenres;

    @OneToMany(mappedBy = "book")
    private List<BookCharacter> bookCharacters;

    @OneToMany(mappedBy = "book")
    private List<BookSetting> bookSetting;

    @OneToMany(mappedBy = "book")
    private List<BookAward> bookAwards;

}
