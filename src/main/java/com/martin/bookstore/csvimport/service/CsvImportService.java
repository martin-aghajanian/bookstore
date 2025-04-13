package com.martin.bookstore.csvimport.service;

import com.martin.bookstore.csvimport.enums.CsvHeader;
import com.martin.bookstore.csvimport.utils.CsvUtils;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

            Map<String, Award> awardDbMap = awardRepository.findAll()
                    .stream().collect(Collectors.toMap(Award::getName, a -> a));

            Map<String, Genre> genreDbMap = genreRepository.findAll()
                    .stream().collect(Collectors.toMap(Genre::getName, g -> g));

            Map<String, Character> characterDbMap = characterRepository.findAll()
                    .stream().collect(Collectors.toMap(Character::getName, c -> c));

            Map<String, Setting> settingDbMap = settingRepository.findAll()
                    .stream().collect(Collectors.toMap(Setting::getName, s -> s));

            Map<String, Author> authorDbMap = authorRepository.findAll()
                    .stream().collect(Collectors.toMap(Author::getFullName, a -> a));

            Map<String, Edition> editionDbMap = editionRepository.findAll()
                    .stream().collect(Collectors.toMap(Edition::getName, e -> e));

            Map<String, Publisher> publisherDbMap = publisherRepository.findAll()
                    .stream().collect(Collectors.toMap(Publisher::getName, p -> p));

            Map<String, Format> formatDbMap = formatRepository.findAll()
                    .stream().collect(Collectors.toMap(Format::getFormat, f -> f));

            Map<String, Language> languageDbMap = languageRepository.findAll()
                    .stream().collect(Collectors.toMap(Language::getName, l -> l));

            Map<String, Series> seriesDbMap = seriesRepository.findAll()
                    .stream().collect(Collectors.toMap(Series::getName, s -> s));

            Map<String, Edition> editions = new HashMap<>();
            Map<String, Language> languages = new HashMap<>();
            Map<String, Publisher> publishers = new HashMap<>();
            Map<String, Format> formats = new HashMap<>();
            Map<String, Series> seriesSet = new HashMap<>();
            Map<String, Award> awards = new HashMap<>();
            Map<String, Genre> genres = new HashMap<>();
            Map<String, Character> characters = new HashMap<>();
            Map<String, Setting> settings = new HashMap<>();
            Map<String, Author> authorsMap = new HashMap<>();

            List<Book> books = new ArrayList<>();

            Set<Long> existingIsbns = bookRepository.findAll()
                    .stream()
                    .map(Book::getIsbn)
                    .collect(Collectors.toSet());

            Set<Long> processedIsbns = new HashSet<>(existingIsbns);

            List<BookAuthor> bookAuthors = new ArrayList<>();
            List<BookAward> bookAwards = new ArrayList<>();
            List<BookGenre> bookGenres = new ArrayList<>();
            List<BookCharacter> bookCharacters = new ArrayList<>();
            List<BookSetting> bookSettings = new ArrayList<>();

            int recordCount = 0;
            for (CSVRecord record : csvParser) {
                recordCount++;

                try {
                    String title = record.get(CsvHeader.TITLE.value()).trim();
                    String isbnStr = record.get(CsvHeader.ISBN.value()).trim();
                    String publishDateStr = record.get(CsvHeader.PUBLISH_DATE.value()).trim();
                    String descriptionStr = record.get(CsvHeader.DESCRIPTION.value()).trim();
                    String coverImageStr = record.get(CsvHeader.COVER_IMAGE.value()).trim();
                    String firstPublishDateStr = record.get(CsvHeader.FIRST_PUBLISH_DATE.value()).trim();
                    String starRatings = record.get(CsvHeader.RATINGS_BY_STARS.value()).replaceAll("[\\[\\]']", "");
                    String editionName = record.get(CsvHeader.EDITION.value()).trim();
                    String languageName = record.get(CsvHeader.LANGUAGE.value()).trim();
                    String publisherName = record.get(CsvHeader.PUBLISHER.value()).trim();
                    String formatName = record.get(CsvHeader.BOOK_FORMAT.value()).trim();
                    String seriesField = record.get(CsvHeader.SERIES.value()).trim();
                    String authorCol = record.get(CsvHeader.AUTHOR.value()).trim();
                    String awardsCol = record.get(CsvHeader.AWARDS.value()).trim();
                    String genreCol = record.get(CsvHeader.GENRES.value()).trim();
                    String charCol = record.get(CsvHeader.CHARACTERS.value()).trim();
                    String settingCol = record.get(CsvHeader.SETTING.value()).trim();
                    String pagesStr = record.get(CsvHeader.PAGES.value());
                    String ratingStr = record.get(CsvHeader.RATING.value());
                    String likedPercentStr = record.get(CsvHeader.LIKED_PERCENT.value());
                    String priceStr = record.get(CsvHeader.PRICE.value());
                    String numRatingsStr = record.get(CsvHeader.NUM_RATINGS.value());
                    String bbeScoreStr = record.get(CsvHeader.BBE_SCORE.value());
                    String bbeVotesStr = record.get(CsvHeader.BBE_VOTES.value());

                    if (title.isEmpty() || !isbnStr.matches("\\d{13}") || publishDateStr.isEmpty()) {
                        System.out.println("skipping invalid record #" + recordCount);
                        continue;
                    }

                    Long isbn = Long.parseLong(isbnStr);
                    if (processedIsbns.contains(isbn)) {
                        continue;
                    }

                    Book book = new Book();
                    book.setTitle(title);
                    book.setIsbn(isbn);
                    book.setDescription(descriptionStr);
                    book.setCoverImageUrl(coverImageStr);

                    LocalDate parsedDate = csvUtils.parseDate(publishDateStr);
                    if (parsedDate == null) continue;
                    book.setPublishDate(parsedDate);

                    LocalDate firstDate = csvUtils.parseDate(firstPublishDateStr);
                    book.setFirstPublishDate(firstDate);

                    csvUtils.tryParseInt(pagesStr, book::setPages);
                    csvUtils.tryParseDouble(ratingStr, book::setRating);
                    csvUtils.tryParseDouble(likedPercentStr, book::setLikedPercentage);
                    csvUtils.tryParseDouble(priceStr, book::setPrice);
                    csvUtils.tryParseLong(numRatingsStr, book::setNumRatings);
                    csvUtils.tryParseLong(bbeScoreStr, book::setBbeScore);
                    csvUtils.tryParseLong(bbeVotesStr, book::setBbeVotes);

                    String[] ratings = starRatings.split(",");
                    if (ratings.length == 5) {
                        csvUtils.tryParseLong(ratings[0], book::setFiveStarRatings);
                        csvUtils.tryParseLong(ratings[1], book::setFourStarRatings);
                        csvUtils.tryParseLong(ratings[2], book::setThreeStarRatings);
                        csvUtils.tryParseLong(ratings[3], book::setTwoStarRatings);
                        csvUtils.tryParseLong(ratings[4], book::setOneStarRatings);
                    }

                    if (!editionName.isEmpty()) {
                        Edition edition = editionDbMap.getOrDefault(editionName, editions.get(editionName));
                        if (edition == null) {
                            edition = new Edition();
                            edition.setName(editionName);
                            editions.put(editionName, edition);
                        }
                        book.setEdition(edition);
                    }

                    if (!languageName.isEmpty()) {
                        Language language = languageDbMap.getOrDefault(languageName, languages.get(languageName));
                        if (language == null) {
                            language = new Language();
                            language.setName(languageName);
                            languages.put(languageName, language);
                        }
                        book.setLanguage(language);
                    }

                    if (!publisherName.isEmpty()) {
                        Publisher publisher = publisherDbMap.getOrDefault(publisherName, publishers.get(publisherName));
                        if (publisher == null) {
                            publisher = new Publisher();
                            publisher.setName(publisherName);
                            publishers.put(publisherName, publisher);
                        }
                        book.setPublisher(publisher);
                    }

                    if (!formatName.isEmpty()) {
                        Format format = formatDbMap.getOrDefault(formatName, formats.get(formatName));
                        if (format == null) {
                            format = new Format();
                            format.setFormat(formatName);
                            formats.put(formatName, format);
                        }
                        book.setFormat(format);
                    }



                    if (!seriesField.isEmpty()) {
                        int hashIndex = seriesField.indexOf("#");
                        String seriesName = (hashIndex != -1)
                                ? seriesField.substring(0, hashIndex).trim()
                                : seriesField.trim();

                        if (!seriesName.isEmpty()) {
                            Series series = seriesDbMap.getOrDefault(seriesName, seriesSet.get(seriesName));
                            if (series == null) {
                                series = new Series();
                                series.setName(seriesName);
                                seriesSet.put(seriesName, series);
                            }
                            book.setSeries(series);
                        }
                    }

                    if (!authorCol.isEmpty()) {
                        List<String> authorEntries = csvUtils.splitAuthors(authorCol);
                        for (String entry : authorEntries) {
                            boolean isGoodreads = false;
                            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(entry);

                            String contribution = m.find() ? m.group(1).trim() : "Author";

                            while (m.find()) {
                                for (String part : m.group(1).split(",")) {
                                    if (part.trim().equalsIgnoreCase("Goodreads Author")) {
                                        isGoodreads = true;
                                    }
                                }
                            }

                            String name = entry.replaceAll("\\s*\\([^)]*\\)", "").trim();
                            boolean finalIsGoodreads = isGoodreads;
                            Author author = authorDbMap.getOrDefault(name, authorsMap.get(name));
                            if (author == null) {
                                author = new Author();
                                author.setFullName(name);
                                author.setGoodReadsAuthor(finalIsGoodreads);
                                authorsMap.put(name, author);
                            } else if (finalIsGoodreads) {
                                author.setGoodReadsAuthor(true);
                            }

                            BookAuthor ba = new BookAuthor();
                            ba.setBook(book);
                            ba.setAuthor(author);
                            ba.setContribution(contribution);
                            bookAuthors.add(ba);
                        }
                    }

                    if (awardsCol.startsWith("[") && awardsCol.endsWith("]"))
                        awardsCol = awardsCol.substring(1, awardsCol.length() - 1);
                    for (String awardStr : awardsCol.split(",")) {
                        awardStr = awardStr.trim().replaceAll("[\"']", "");
                        int lastParen = awardStr.lastIndexOf(" (");
                        if (lastParen != -1) {
                            String name = awardStr.substring(0, lastParen).trim();
                            String yearStr = awardStr.substring(lastParen + 2).replace(")", "").trim();

                            Award award = awardDbMap.getOrDefault(name, awards.get(name));
                            if (award == null) {
                                award = new Award();
                                award.setName(name);
                                awards.put(name, award);
                            }

                            try {
                                int year = Integer.parseInt(yearStr);
                                BookAward ba = new BookAward();
                                ba.setBook(book);
                                ba.setAward(award);
                                ba.setYear(LocalDate.of(year, 1, 1));
                                bookAwards.add(ba);
                            } catch (NumberFormatException ignored) {}
                        }
                    }

                    if (genreCol.startsWith("[") && genreCol.endsWith("]"))
                        genreCol = genreCol.substring(1, genreCol.length() - 1);
                    for (String g : genreCol.split(",")) {
                        g = g.replaceAll("[\"']", "").trim();
                        if (!g.isEmpty()) {
                            Genre genre = genreDbMap.getOrDefault(g, genres.get(g));
                            if (genre == null) {
                                genre = new Genre();
                                genre.setName(g);
                                genres.put(g, genre);
                            }
                            BookGenre bg = new BookGenre();
                            bg.setBook(book);
                            bg.setGenre(genre);
                            bookGenres.add(bg);
                        }
                    }

                    if (charCol.startsWith("[") && charCol.endsWith("]"))
                        charCol = charCol.substring(1, charCol.length() - 1);
                    for (String c : charCol.split(",")) {
                        c = c.replaceAll("[\"']", "").trim();
                        if (!c.isEmpty()) {
                            Character character = characterDbMap.getOrDefault(c, characters.get(c));
                            if (character == null) {
                                character = new Character();
                                character.setName(c);
                                characters.put(c, character);
                            }
                            BookCharacter bc = new BookCharacter();
                            bc.setBook(book);
                            bc.setCharacter(character);
                            bookCharacters.add(bc);
                        }
                    }

                    if (settingCol.startsWith("[") && settingCol.endsWith("]"))
                        settingCol = settingCol.substring(1, settingCol.length() - 1);
                    for (String s : csvUtils.parseSettings(settingCol)) {
                        if (!s.isEmpty()) {
                            Setting setting = settingDbMap.getOrDefault(s, settings.get(s));
                            if (setting == null) {
                                setting = new Setting();
                                setting.setName(s);
                                settings.put(s, setting);
                            }
                            BookSetting bs = new BookSetting();
                            bs.setBook(book);
                            bs.setSetting(setting);
                            bookSettings.add(bs);
                        }
                    }

                    books.add(book);
                    processedIsbns.add(isbn);

                } catch (Exception e) {
                    System.err.println("error on record #" + recordCount + ": " + e.getMessage());
                }
            }

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
            bookRepository.saveAll(books);
            bookAuthorRepository.saveAll(bookAuthors);
            bookAwardRepository.saveAll(bookAwards);
            bookGenreRepository.saveAll(bookGenres);
            bookCharacterRepository.saveAll(bookCharacters);
            bookSettingRepository.saveAll(bookSettings);

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file", e);
        }
    }
}
