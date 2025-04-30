package com.martin.bookstore.service;

import com.martin.bookstore.core.enums.CsvHeader;
import com.martin.bookstore.core.exception.CsvProcessingException;
import com.martin.bookstore.core.utils.CsvUtils;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final CsvUtils csvUtils;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AwardRepository awardRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final SettingRepository settingRepository;
    private final FormatRepository formatRepository;
    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final SeriesRepository seriesRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookAwardRepository bookAwardRepository;
    private final BookGenreRepository bookGenreRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final BookSettingRepository bookSettingRepository;

    public void importCsv(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            csvUtils.validateCsvHeaders(csvParser);

            Map<String, Award> awardDbMap = new ConcurrentHashMap<>(awardRepository.findAll()
                    .stream().collect(Collectors.toMap(Award::getName, a -> a)));

            Map<String, Genre> genreDbMap = new ConcurrentHashMap<>(genreRepository.findAll()
                    .stream().collect(Collectors.toMap(Genre::getName, g -> g)));

            Map<String, Character> characterDbMap = new ConcurrentHashMap<>(characterRepository.findAll()
                    .stream().collect(Collectors.toMap(Character::getName, c -> c)));

            Map<String, Setting> settingDbMap = new ConcurrentHashMap<>(settingRepository.findAll()
                    .stream().collect(Collectors.toMap(Setting::getName, s -> s)));

            Map<String, Author> authorDbMap = new ConcurrentHashMap<>(authorRepository.findAll()
                    .stream().collect(Collectors.toMap(Author::getFullName, a -> a)));

            Map<String, Edition> editionDbMap = new ConcurrentHashMap<>(editionRepository.findAll()
                    .stream().collect(Collectors.toMap(Edition::getName, e -> e)));

            Map<String, Publisher> publisherDbMap = new ConcurrentHashMap<>(publisherRepository.findAll()
                    .stream().collect(Collectors.toMap(Publisher::getName, p -> p)));

            Map<String, Format> formatDbMap = new ConcurrentHashMap<>(formatRepository.findAll()
                    .stream().collect(Collectors.toMap(Format::getFormat, f -> f)));

            Map<String, Language> languageDbMap = new ConcurrentHashMap<>(languageRepository.findAll()
                    .stream().collect(Collectors.toMap(Language::getName, l -> l)));

            Map<String, Series> seriesDbMap = new ConcurrentHashMap<>(seriesRepository.findAll()
                    .stream().collect(Collectors.toMap(Series::getName, s -> s)));

            Map<String, Edition> editions = new ConcurrentHashMap<>();
            Map<String, Language> languages = new ConcurrentHashMap<>();
            Map<String, Publisher> publishers = new ConcurrentHashMap<>();
            Map<String, Format> formats = new ConcurrentHashMap<>();
            Map<String, Series> seriesSet = new ConcurrentHashMap<>();
            Map<String, Award> awards = new ConcurrentHashMap<>();
            Map<String, Genre> genres = new ConcurrentHashMap<>();
            Map<String, Character> characters = new ConcurrentHashMap<>();
            Map<String, Setting> settings = new ConcurrentHashMap<>();
            Map<String, Author> authorsMap = new ConcurrentHashMap<>();

            Set<Long> existingIsbns = bookRepository.findAll()
                    .stream()
                    .map(Book::getIsbn)
                    .collect(Collectors.toSet());

            Set<Long> processedIsbns = Collections.synchronizedSet(new HashSet<>(existingIsbns));

            Queue<Book> books = new ConcurrentLinkedQueue<>();
            Queue<BookAuthor> bookAuthors = new ConcurrentLinkedQueue<>();
            Queue<BookAward> bookAwards = new ConcurrentLinkedQueue<>();
            Queue<BookGenre> bookGenres = new ConcurrentLinkedQueue<>();
            Queue<BookCharacter> bookCharacters = new ConcurrentLinkedQueue<>();
            Queue<BookSetting> bookSettings = new ConcurrentLinkedQueue<>();

            ConcurrentLinkedQueue<String> failedRecords = new ConcurrentLinkedQueue<>();

            ExecutorService executor = Executors.newFixedThreadPool(4);

            List<CSVRecord> records = csvParser.getRecords();

            CountDownLatch latch = new CountDownLatch(records.size());

            for (int i = 0; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                int recordNumber = i + 1;

                executor.submit(() -> {
                    try {
                        processRecord(record, recordNumber,
                                awardDbMap, genreDbMap, characterDbMap, settingDbMap, authorDbMap,
                                editionDbMap, publisherDbMap, formatDbMap, languageDbMap, seriesDbMap,
                                processedIsbns, editions, languages, publishers, formats, seriesSet,
                                awards, genres, characters, settings, authorsMap,
                                books, bookAuthors, bookAwards, bookGenres, bookCharacters, bookSettings
                        );
                    } catch (Exception ex) {
                        failedRecords.add("Record #" + recordNumber + ": " + record.toString());
                    } finally {
                        latch.countDown();
                    }
                });
            }


            latch.await();
            executor.shutdown();

            editionRepository.saveAll(new ArrayList<>(editions.values()));
            languageRepository.saveAll(new ArrayList<>(languages.values()));
            publisherRepository.saveAll(new ArrayList<>(publishers.values()));
            formatRepository.saveAll(new ArrayList<>(formats.values()));
            seriesRepository.saveAll(new ArrayList<>(seriesSet.values()));
            authorRepository.saveAll(new ArrayList<>(authorsMap.values()));
            awardRepository.saveAll(new ArrayList<>(awards.values()));
            genreRepository.saveAll(new ArrayList<>(genres.values()));
            characterRepository.saveAll(new ArrayList<>(characters.values()));
            settingRepository.saveAll(new ArrayList<>(settings.values()));

            bookRepository.saveAll(new ArrayList<>(books));

            bookAuthorRepository.saveAll(new ArrayList<>(bookAuthors));
            bookAwardRepository.saveAll(new ArrayList<>(bookAwards));
            bookGenreRepository.saveAll(new ArrayList<>(bookGenres));
            bookCharacterRepository.saveAll(new ArrayList<>(bookCharacters));
            bookSettingRepository.saveAll(new ArrayList<>(bookSettings));

            if (!failedRecords.isEmpty()) {
                System.out.println("Total failed records: " + failedRecords.size());
                // add to a .txt
            }

        } catch (Exception e) {
            throw new CsvProcessingException("error processing CSV file", e);
        }
    }

    private void processRecord(
            CSVRecord record,
            int recordCount,
            Map<String, Award> awardDbMap,
            Map<String, Genre> genreDbMap,
            Map<String, Character> characterDbMap,
            Map<String, Setting> settingDbMap,
            Map<String, Author> authorDbMap,
            Map<String, Edition> editionDbMap,
            Map<String, Publisher> publisherDbMap,
            Map<String, Format> formatDbMap,
            Map<String, Language> languageDbMap,
            Map<String, Series> seriesDbMap,
            Set<Long> processedIsbns,
            Map<String, Edition> editions,
            Map<String, Language> languages,
            Map<String, Publisher> publishers,
            Map<String, Format> formats,
            Map<String, Series> seriesSet,
            Map<String, Award> awards,
            Map<String, Genre> genres,
            Map<String, Character> characters,
            Map<String, Setting> settings,
            Map<String, Author> authorsMap,
            Queue<Book> books,
            Queue<BookAuthor> bookAuthors,
            Queue<BookAward> bookAwards,
            Queue<BookGenre> bookGenres,
            Queue<BookCharacter> bookCharacters,
            Queue<BookSetting> bookSettings
    ) {
        try {
            String title = record.get(CsvHeader.TITLE.value()).trim();
            String isbnStr = record.get(CsvHeader.ISBN.value()).trim();
            String publishDateStr = record.get(CsvHeader.PUBLISH_DATE.value()).trim();
            String descriptionStr = record.get(CsvHeader.DESCRIPTION.value()).trim();
            String coverImageStr = record.get(CsvHeader.COVER_IMAGE.value()).trim();
            String firstPublishDateStr = record.get(CsvHeader.FIRST_PUBLISH_DATE.value()).trim();
            String starRatings = record.get(CsvHeader.RATINGS_BY_STARS.value()).replaceAll("[\\[\\]']", "");

            String pagesStr = record.get(CsvHeader.PAGES.value());
            String ratingStr = record.get(CsvHeader.RATING.value());
            String likedPercentStr = record.get(CsvHeader.LIKED_PERCENT.value());
            String priceStr = record.get(CsvHeader.PRICE.value());
            String numRatingsStr = record.get(CsvHeader.NUM_RATINGS.value());
            String bbeScoreStr = record.get(CsvHeader.BBE_SCORE.value());
            String bbeVotesStr = record.get(CsvHeader.BBE_VOTES.value());

            csvUtils.validateRequiredFields(recordCount, title, isbnStr, publishDateStr);

            Long isbn;
            try {
                isbn = Long.parseLong(isbnStr);
            } catch (NumberFormatException e) {
                throw new CsvProcessingException("Record #" + recordCount + " has invalid ISBN format: " + isbnStr, e);
            }

            synchronized (processedIsbns) {
                if (processedIsbns.contains(isbn)) {
                    throw new CsvProcessingException("Record #" + recordCount + " has duplicate ISBN: " + isbn);
                }
                processedIsbns.add(isbn);
            }

            Book book = new Book();
            book.setTitle(title);
            book.setIsbn(isbn);
            book.setDescription(descriptionStr);
            book.setCoverImageUrl(coverImageStr);

            LocalDate parsedDate = csvUtils.parseDate(publishDateStr);
            if (parsedDate == null) {
                throw new CsvProcessingException("Record #" + recordCount + " has unparseable publish date: " + publishDateStr);
            }
            book.setPublishDate(parsedDate);

            LocalDate firstDate = csvUtils.parseDate(firstPublishDateStr);
            book.setFirstPublishDate(firstDate);

            csvUtils.tryParseInt(pagesStr, book::setPages, recordCount, CsvHeader.PAGES.value());
            csvUtils.tryParseDouble(ratingStr, book::setRating, recordCount, CsvHeader.RATING.value());
            csvUtils.tryParseDouble(likedPercentStr, book::setLikedPercentage, recordCount, CsvHeader.LIKED_PERCENT.value());
            csvUtils.tryParseDouble(priceStr, book::setPrice, recordCount, CsvHeader.PRICE.value());
            csvUtils.tryParseLong(numRatingsStr, book::setNumRatings, recordCount, CsvHeader.NUM_RATINGS.value());
            csvUtils.tryParseLong(bbeScoreStr, book::setBbeScore, recordCount, CsvHeader.BBE_SCORE.value());
            csvUtils.tryParseLong(bbeVotesStr, book::setBbeVotes, recordCount, CsvHeader.BBE_VOTES.value());

            String[] ratings = starRatings.split(",");
            if (ratings.length == 5) {
                csvUtils.tryParseLong(ratings[0], book::setFiveStarRatings, recordCount, CsvHeader.RATINGS_BY_STARS.value() + " (5-star)");
                csvUtils.tryParseLong(ratings[1], book::setFourStarRatings, recordCount, CsvHeader.RATINGS_BY_STARS.value() + " (4-star)");
                csvUtils.tryParseLong(ratings[2], book::setThreeStarRatings, recordCount, CsvHeader.RATINGS_BY_STARS.value() + " (3-star)");
                csvUtils.tryParseLong(ratings[3], book::setTwoStarRatings, recordCount, CsvHeader.RATINGS_BY_STARS.value() + " (2-star)");
                csvUtils.tryParseLong(ratings[4], book::setOneStarRatings, recordCount, CsvHeader.RATINGS_BY_STARS.value() + " (1-star)");
            }

            csvUtils.resolveEdition(book, record.get(CsvHeader.EDITION.value()).trim(), editionDbMap, editions);
            csvUtils.resolveLanguage(book, record.get(CsvHeader.LANGUAGE.value()).trim(), languageDbMap, languages);
            csvUtils.resolvePublisher(book, record.get(CsvHeader.PUBLISHER.value()).trim(), publisherDbMap, publishers);
            csvUtils.resolveFormat(book, record.get(CsvHeader.BOOK_FORMAT.value()).trim(), formatDbMap, formats);
            csvUtils.resolveSeries(book, record.get(CsvHeader.SERIES.value()).trim(), seriesDbMap, seriesSet);
            csvUtils.resolveAuthors(book, record.get(CsvHeader.AUTHOR.value()).trim(), authorDbMap, authorsMap, bookAuthors);
            csvUtils.resolveAwards(book, record.get(CsvHeader.AWARDS.value()).trim(), awardDbMap, awards, bookAwards);
            csvUtils.resolveGenres(book, record.get(CsvHeader.GENRES.value()).trim(), genreDbMap, genres, bookGenres);
            csvUtils.resolveCharacters(book, record.get(CsvHeader.CHARACTERS.value()).trim(), characterDbMap, characters, bookCharacters);
            csvUtils.resolveSettings(book, record.get(CsvHeader.SETTING.value()).trim(), settingDbMap, settings, bookSettings);

            books.add(book);

        } catch (Exception e) {
            throw new CsvProcessingException("failed to process record #" + recordCount, e);
        }
    }
}
