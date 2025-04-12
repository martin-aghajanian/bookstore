package com.martin.bookstore.csvimport.enums;

public enum CsvHeader {
    TITLE("title"),
    SERIES("series"),
    AUTHOR("author"),
    RATING("rating"),
    DESCRIPTION("description"),
    LANGUAGE("language"),
    ISBN("isbn"),
    GENRES("genres"),
    CHARACTERS("characters"),
    BOOK_FORMAT("bookFormat"),
    EDITION("edition"),
    PAGES("pages"),
    PUBLISHER("publisher"),
    PUBLISH_DATE("publishDate"),
    FIRST_PUBLISH_DATE("firstPublishDate"),
    AWARDS("awards"),
    NUM_RATINGS("numRatings"),
    RATINGS_BY_STARS("ratingsByStars"),
    LIKED_PERCENT("likedPercent"),
    SETTING("setting"),
    COVER_IMAGE("coverImg"),
    BBE_SCORE("bbeScore"),
    BBE_VOTES("bbeVotes"),
    PRICE("price");

    private final String value;

    CsvHeader(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
