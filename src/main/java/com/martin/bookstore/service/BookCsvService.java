package com.martin.bookstore.service;

import com.martin.bookstore.persistence.entity.*;
import com.martin.bookstore.persistence.repository.*;
import com.martin.bookstore.service.util.CsvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;


@Service
public class BookCsvService {

    private final CsvUtils csvUtils;

    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final BookFormatRepository bookFormatRepository;
    private final BookGenreRepository bookGenreRepository;
    private final BookSettingRepository bookSettingRepository;
    private final CharacterRepository characterRepository;
    private final EditionRepository editionRepository;
    private final GenreRepository genreRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final SettingRepository settingRepository;
    private final AuthorRepository authorRepository;
    private final AwardRepository awardRepository;
    private final BookAwardRepository bookAwardRepository;


    @Autowired
    public BookCsvService(CsvUtils csvUtils, BookRepository bookRepository, BookAuthorRepository bookAuthorRepository, BookCharacterRepository bookCharacterRepository, BookFormatRepository bookFormatRepository, BookGenreRepository bookGenreRepository, BookSettingRepository bookSettingRepository, CharacterRepository characterRepository, EditionRepository editionRepository, GenreRepository genreRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, SeriesRepository seriesRepository, SettingRepository settingRepository, AuthorRepository authorRepository, AwardRepository awardRepository, BookAwardRepository bookAwardRepository) {
        this.csvUtils = csvUtils;
        this.bookRepository = bookRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookCharacterRepository = bookCharacterRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.bookGenreRepository = bookGenreRepository;
        this.bookSettingRepository = bookSettingRepository;
        this.characterRepository = characterRepository;
        this.editionRepository = editionRepository;
        this.genreRepository = genreRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.seriesRepository = seriesRepository;
        this.settingRepository = settingRepository;
        this.authorRepository = authorRepository;
        this.awardRepository = awardRepository;
        this.bookAwardRepository = bookAwardRepository;
    }


    public void processCsvFile(MultipartFile file) {

        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("Error reading file bytes", e);
        }

        // Process many-to-one relations (editions, series, languages, publishers, book_formats)

        try (Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileBytes)));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            Set<String> requiredHeaders = Set.of(
                    "title", "series", "author", "rating", "description", "language",
                    "isbn", "genres", "characters", "bookFormat", "edition", "pages", "publisher", "publishDate",
                    "firstPublishDate", "awards", "numRatings", "ratingsByStars", "likedPercent", "setting",
                    "coverImg", "bbeScore", "bbeVotes", "price"
            );

            for (String header : requiredHeaders) {
                if (!csvParser.getHeaderMap().containsKey(header)) {
                    throw new RuntimeException("csv file is missing required header: " + header);
                }
            }

            Set<String> uniqueEditions = new HashSet<>();
            Set<String> uniqueLanguages = new HashSet<>();
            Set<String> uniquePublishers = new HashSet<>();
            Set<String> uniqueBookFormats = new HashSet<>();
            Set<String> uniqueSeries = new HashSet<>();

            for (CSVRecord record : csvParser) {

                // editions

                String editionName = record.get("edition").trim();
                if (!editionName.isEmpty()) {
                    uniqueEditions.add(editionName);
                }

                // languages

                String languageName = record.get("language").trim();
                if (!languageName.isEmpty()) {
                    uniqueLanguages.add(languageName);
                }

                // publishers

                String publisherName = record.get("publisher").trim();
                if (!publisherName.isEmpty()) {
                    uniquePublishers.add(publisherName);
                }

                // book formats

                String bookFormat = record.get("bookFormat").trim();
                if (!bookFormat.isEmpty()) {
                    uniqueBookFormats.add(bookFormat);
                }

                // series

                String seriesName = record.get("series").trim();
                if (!seriesName.isEmpty()) {
                    int hashIndex = seriesName.indexOf("#");
                    if (hashIndex != -1) {
                        seriesName = seriesName.substring(0, hashIndex).trim();
                    }
                    if (!seriesName.isEmpty()) {
                        uniqueSeries.add(seriesName);
                    }
                }

            }

            // save editions

            List<Edition> editionsToSave = new ArrayList<>();
            for (String editionName : uniqueEditions) {
                Edition edition = editionRepository.findByName(editionName).orElse(new Edition());
                edition.setName(editionName);
                editionsToSave.add(edition);
            }
            csvUtils.batchSave(editionsToSave, editionRepository);

            // save languages

            List<Language> languagesToSave = new ArrayList<>();
            for (String languageName : uniqueLanguages) {
                Language language = languageRepository.findByName(languageName).orElse(new Language());
                language.setName(languageName);
                languagesToSave.add(language);
            }
            csvUtils.batchSave(languagesToSave, languageRepository);

            // save publishers

            List<Publisher> publishersToSave = new ArrayList<>();
            for (String publisherName : uniquePublishers) {
                Publisher publisher = publisherRepository.findByName(publisherName).orElse(new Publisher());
                publisher.setName(publisherName);
                publishersToSave.add(publisher);
            }
            csvUtils.batchSave(publishersToSave, publisherRepository);

            // save book formats

            List<BookFormat> formatsToSave = new ArrayList<>();
            for (String bookFormat : uniqueBookFormats) {
                BookFormat format = bookFormatRepository.findByFormat(bookFormat).orElse(new BookFormat());
                format.setFormat(bookFormat);
                formatsToSave.add(format);
            }
            csvUtils.batchSave(formatsToSave, bookFormatRepository);

            // save series

            List<Series> seriesToSave = new ArrayList<>();
            for (String seriesName : uniqueSeries) {
                Series series = seriesRepository.findByName(seriesName).orElse(new Series());
                series.setName(seriesName);
                seriesToSave.add(series);
            }
            csvUtils.batchSave(seriesToSave, seriesRepository);

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV for many-to-one relations", e);
        }

        // Process books
        try (Reader bookReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileBytes)));
             CSVParser bookParser = new CSVParser(bookReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<Book> booksToSave = new ArrayList<>();
            Set<Long> processedIsbns = new HashSet<>();


            int recordCount = 0;
            for (CSVRecord record : bookParser) {
                recordCount++;
                try {
                    Book book = new Book();

                    // Required not null fields validation
                    String title = record.get("title").trim();
                    String isbnStr = record.get("isbn").trim();
                    String publishDateStr = record.get("publishDate").trim();

                    // validations
                    if (!isbnStr.matches("\\d{13}")) {
                        System.out.println("Invalid ISBN format for record #" + recordCount + ": " + isbnStr + ". Skipping.");
                        continue;
                    }

                    if (title.isEmpty()) {
                        System.out.println("Missing required field 'title' in record #" + recordCount + ". Skipping.");
                        continue;
                    }
                    if (isbnStr.isEmpty()) {
                        System.out.println("Missing required field 'isbn' in record #" + recordCount + ". Skipping.");
                        continue;
                    }
                    if (publishDateStr.isEmpty()) {
                        System.out.println("Missing required field 'publishDate' in record #" + recordCount + ". Skipping.");
                        continue;
                    }

                    book.setTitle(title);
                    book.setDescription(record.get("description").trim());

                    try {
                        book.setIsbn(Long.parseLong(isbnStr));
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException("Invalid ISBN in record #" + recordCount + ": " + isbnStr);
                    }

                    try {
                        book.setPages(Integer.parseInt(record.get("pages").trim()));
                    } catch (NumberFormatException nfe) {
                        // Optional field, can skip if invalid
                    }

                    try {
                        book.setRating(Double.parseDouble(record.get("rating").trim()));
                    } catch (NumberFormatException nfe) { }

                    try {
                        book.setLikedPercentage(Double.parseDouble(record.get("likedPercent").trim()));
                    } catch (NumberFormatException nfe) { }

                    try {
                        book.setPrice(Double.parseDouble(record.get("price").trim()));
                    } catch (NumberFormatException nfe) { }


                    // publish date

                    String cleanedPublishDateStr = publishDateStr.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
                    List<DateTimeFormatter> dateFormatters = new ArrayList<>();

                    if (publishDateStr.contains("/")) {
                        dateFormatters.add(DateTimeFormatter.ofPattern("MM/dd/yy"));
                    } else {
                        dateFormatters.add(DateTimeFormatter.ISO_LOCAL_DATE);
                    }

                    dateFormatters.add(DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH));
                    dateFormatters.add(new DateTimeFormatterBuilder()
                            .appendPattern("MMMM yyyy")
                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                            .toFormatter(Locale.ENGLISH));
                    dateFormatters.add(new DateTimeFormatterBuilder()
                            .appendPattern("yyyy")
                            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                            .toFormatter(Locale.ENGLISH));


                    LocalDate parsedPublishDate = null;
                    for (DateTimeFormatter formatter : dateFormatters) {
                        try {
                            parsedPublishDate = LocalDate.parse(cleanedPublishDateStr, formatter);
                            break;
                        } catch (Exception e) { }
                    }

                    if (parsedPublishDate == null) {
                        System.out.println("Invalid publishDate format for record #" + recordCount + ": " + publishDateStr + ". Skipping.");
                        continue;
                    }

                    book.setPublishDate(parsedPublishDate);

                    // first publish date

                    String firstPublishDate = record.get("firstPublishDate").trim();
                    if (!firstPublishDate.isEmpty()) {
                        DateTimeFormatter firstPublishFormatter = firstPublishDate.contains("/") ?
                                DateTimeFormatter.ofPattern("MM/dd/yy") : DateTimeFormatter.ISO_LOCAL_DATE;
                        try {
                            book.setFirstPublishDate(LocalDate.parse(firstPublishDate, firstPublishFormatter));
                        } catch (Exception ex) {
                            // log error
                        }
                    }

                    // ratings by stars

                    String ratingsByStarsString = record.get("ratingsByStars").trim();
                    if (!ratingsByStarsString.isEmpty()) {
                        ratingsByStarsString = ratingsByStarsString.replaceAll("[\\[\\]']", "");
                        String[] ratingValues = ratingsByStarsString.split("\\s*,\\s*");
                        if (ratingValues.length >= 5) {
                            try {
                                book.setFiveStarRatings(Long.parseLong(ratingValues[0].trim()));
                                book.setFourStarRatings(Long.parseLong(ratingValues[1].trim()));
                                book.setThreeStarRatings(Long.parseLong(ratingValues[2].trim()));
                                book.setTwoStarRatings(Long.parseLong(ratingValues[3].trim()));
                                book.setOneStarRatings(Long.parseLong(ratingValues[4].trim()));
                            } catch (NumberFormatException nfe) {
                                // log error
                            }
                        }
                    }

                    try {
                        book.setNumRatings(Long.parseLong(record.get("numRatings").trim()));
                    } catch (NumberFormatException nfe) { }

                    try {
                        book.setBbeScore(Long.parseLong(record.get("bbeScore").trim()));
                    } catch (NumberFormatException nfe) { }

                    try {
                        book.setBbeVotes(Long.parseLong(record.get("bbeVotes").trim()));
                    } catch (NumberFormatException nfe) { }

                    book.setCoverImageUrl(record.get("coverImg").trim());

                    // many to one relations: Edition, Language, Publisher, BookFormat
                    String editionName = record.get("edition").trim();
                    if (!editionName.isEmpty()) {
                        Edition edition = editionRepository.findByName(editionName).orElse(null);
                        book.setEdition(edition);
                    }

                    String languageName = record.get("language").trim();
                    if (!languageName.isEmpty()) {
                        Language language = languageRepository.findByName(languageName).orElse(null);
                        book.setLanguage(language);
                    }

                    String publisherName = record.get("publisher").trim();
                    if (!publisherName.isEmpty()) {
                        Publisher publisher = publisherRepository.findByName(publisherName).orElse(null);
                        book.setPublisher(publisher);
                    }

                    String formatName = record.get("bookFormat").trim();
                    if (!formatName.isEmpty()) {
                        BookFormat format = bookFormatRepository.findByFormat(formatName).orElse(null);
                        book.setBookFormat(format);
                    }

                    // Series processing
                    String seriesField = record.get("series").trim();
                    if (!seriesField.isEmpty()) {
                        String seriesName = seriesField;
                        int hashIndex = seriesField.indexOf("#");
                        if (hashIndex != -1) {
                            seriesName = seriesField.substring(0, hashIndex).trim();
                        }
                        Series series = seriesRepository.findByName(seriesName).orElse(null);
                        book.setSeries(series);
                    }

                    // Skip duplicate books by ISBN
                    if (processedIsbns.contains(book.getIsbn()) || bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
                        System.out.println("Duplicate ISBN found for record #" + recordCount + ": " + book.getIsbn() + ". Skipping.");
                        continue;
                    }

                    processedIsbns.add(book.getIsbn());
                    booksToSave.add(book);

                } catch (Exception e) {
                    System.err.println("Error processing record #" + recordCount + ": " + e.getMessage());
                }
            }

            csvUtils.batchSave(booksToSave, bookRepository);

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV for books", e);
        }


        // Process many-to-many relations if needed...
    }
}
