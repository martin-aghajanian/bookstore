package com.martin.bookstore.service;

import com.martin.bookstore.persistence.entity.*;
import com.martin.bookstore.persistence.entity.Character;
import com.martin.bookstore.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class BookCsvService {

    private static final int BATCH_SIZE = 5000;

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


    @Autowired
    public BookCsvService(BookRepository bookRepository, BookAuthorRepository bookAuthorRepository, BookCharacterRepository bookCharacterRepository, BookFormatRepository bookFormatRepository, BookGenreRepository bookGenreRepository, BookSettingRepository bookSettingRepository, CharacterRepository characterRepository, EditionRepository editionRepository, GenreRepository genreRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, SeriesRepository seriesRepository, SettingRepository settingRepository, AuthorRepository authorRepository) {
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
    }

    private <T> void batchSave(List<T> entities, JpaRepository<T, ?> repository) {
        for (int i = 0; i < entities.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, entities.size());
            repository.saveAll(entities.subList(i, endIndex));
        }
    }

    private List<String> parseSettings(String input) {
        List<String> settings = new ArrayList<>();
        Pattern pattern = Pattern.compile("'([^']*)'|\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String setting = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            if (setting != null && !setting.trim().isEmpty()){
                settings.add(setting.trim());
            }
        }
        return settings;
    }

    private List<String> splitAuthors(String authorsColumn) {
        List<String> authors = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int parenthesesLevel = 0;
        for (int i = 0; i < authorsColumn.length(); i++) {
            char ch = authorsColumn.charAt(i);
            if (ch == '(') {
                parenthesesLevel++;
            } else if (ch == ')') {
                if (parenthesesLevel > 0) {
                    parenthesesLevel--;
                }
            }
            // Only split on comma if we are not inside parentheses.
            if (ch == ',' && parenthesesLevel == 0) {
                authors.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        if (current.length() > 0) {
            authors.add(current.toString().trim());
        }
        return authors;
    }

    public void processCsvFile(MultipartFile file) {

        // process many to one relations (editions, series, languages, publishers, book_formats)

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
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
            Set<String> uniqueGenres = new HashSet<>();
            Set<String> uniqueCharacters = new HashSet<>();
            Set<String> uniqueSettings = new HashSet<>();
            Map<String, Author> authorsMap = new HashMap<>();



            for (CSVRecord record : csvParser) {
                String editionName = record.get("edition").trim();
                if (!editionName.isEmpty()) {
                    uniqueEditions.add(editionName);
                }

                String languageName = record.get("language").trim();
                if (!languageName.isEmpty()) {
                    uniqueLanguages.add(languageName);
                }

                String publisherName = record.get("publisher").trim();
                if (!publisherName.isEmpty()) {
                    uniquePublishers.add(publisherName);
                }

                String bookFormat = record.get("bookFormat").trim();
                if (!bookFormat.isEmpty()) {
                    uniqueBookFormats.add(bookFormat);
                }

                String seriesName = record.get("series").trim();
                if (!seriesName.isEmpty()) {
                    uniqueSeries.add(seriesName);
                }

                String genresColumn = record.get("genres").trim();
                if (!genresColumn.isEmpty()) {
                    if (genresColumn.startsWith("[") && genresColumn.endsWith("]")) {
                        genresColumn = genresColumn.substring(1, genresColumn.length() - 1);
                    }

                    String[] genresArray = genresColumn.split(",");
                    for (String genreStr : genresArray) {

                        genreStr = genreStr.replace("'", "").replace("\"", "").trim();
                        if (!genreStr.isEmpty()) {
                            uniqueGenres.add(genreStr);
                        }

                    }
                }

                String characterColumn = record.get("characters").trim();
                if (!characterColumn.isEmpty()) {
                    if (characterColumn.startsWith("[") && characterColumn.endsWith("]")) {
                        characterColumn = characterColumn.substring(1, characterColumn.length() - 1);
                    }

                    String[] charactersArray = characterColumn.split(",");
                    for (String characterStr : charactersArray) {

                        characterStr = characterStr.replace("'", "").trim();
                        if (!characterStr.isEmpty()) {
                            uniqueCharacters.add(characterStr);
                        }

                    }
                }

                String settingsColumn = record.get("setting").trim();
                if (!settingsColumn.isEmpty()) {
                    if (settingsColumn.startsWith("[") && settingsColumn.endsWith("]")) {
                        settingsColumn = settingsColumn.substring(1, settingsColumn.length() - 1);
                    }
                    List<String> settingsList = parseSettings(settingsColumn);
                    uniqueSettings.addAll(settingsList);
                }

                String authorsColumn = record.get("author").trim();
                if (!authorsColumn.isEmpty()) {
                    List<String> authorEntries = splitAuthors(authorsColumn);
                    for (String authorEntry : authorEntries) {
                        boolean isGoodreads = false;
                        String namePart = authorEntry;
                        Pattern pattern = Pattern.compile("^(.*?)\\s*\\((.*?)\\)$");
                        Matcher matcher = pattern.matcher(authorEntry);
                        if (matcher.find()) {
                            namePart = matcher.group(1).trim();
                            String contributionsStr = matcher.group(2).trim();
                            String[] contributions = contributionsStr.split(",");
                            for (String contribution : contributions) {
                                if ("Goodreads Author".equalsIgnoreCase(contribution.trim())) {
                                    isGoodreads = true;
                                    break;
                                }
                            }
                        }
                        String key = namePart;
                        if (authorsMap.containsKey(key)) {
                            if (isGoodreads) {
                                authorsMap.get(key).setGoodReadsAuthor(true);
                            }
                        } else {
                            Author author = new Author();
                            author.setFullName(namePart);
                            author.setGoodReadsAuthor(isGoodreads);
                            authorsMap.put(key, author);
                        }
                    }
                }

            }

            List<Edition> editionsToSave = new ArrayList<>();
            for (String editionName : uniqueEditions) {
                Edition edition = editionRepository.findByName(editionName).orElse(new Edition());
                edition.setName(editionName);
                editionsToSave.add(edition);
            }
            batchSave(editionsToSave, editionRepository);

            List<Language> languagesToSave = new ArrayList<>();
            for (String languageName : uniqueLanguages) {
                Language language = languageRepository.findByName(languageName).orElse(new Language());
                language.setName(languageName);
                languagesToSave.add(language);
            }
            batchSave(languagesToSave, languageRepository);

            List<Publisher> publishersToSave = new ArrayList<>();
            for (String publisherName : uniquePublishers) {
                Publisher publisher = publisherRepository.findByName(publisherName).orElse(new Publisher());
                publisher.setName(publisherName);
                publishersToSave.add(publisher);
            }
            batchSave(publishersToSave, publisherRepository);

            List<BookFormat> formatsToSave = new ArrayList<>();
            for (String bookFormat : uniqueBookFormats) {
                BookFormat format = bookFormatRepository.findByFormat(bookFormat).orElse(new BookFormat());
                format.setFormat(bookFormat);
                formatsToSave.add(format);
            }
            batchSave(formatsToSave, bookFormatRepository);

            List<Series> seriesToSave = new ArrayList<>();
            for (String seriesName : uniqueSeries) {
                Series series = seriesRepository.findByName(seriesName).orElse(new Series());
                series.setName(seriesName);
                seriesToSave.add(series);
            }
            batchSave(seriesToSave, seriesRepository);

            List<Genre> genresToSave = new ArrayList<>();
            for (String genreName : uniqueGenres) {
                Genre genre = genreRepository.findByName(genreName).orElse(new Genre());
                genre.setName(genreName);
                genresToSave.add(genre);
            }
            batchSave(genresToSave, genreRepository);

            List<Character> charactersToSave = new ArrayList<>();
            for (String characterName : uniqueCharacters) {
                Character character = characterRepository.findByName(characterName).orElse(new Character());
                character.setName(characterName);
                charactersToSave.add(character);
            }
            batchSave(charactersToSave, characterRepository);

            List<Setting> settingsToSave = new ArrayList<>();
            for (String settingName : uniqueSettings) {
                Setting setting = settingRepository.findByName(settingName).orElse(new Setting());
                setting.setName(settingName);
                settingsToSave.add(setting);
            }
            batchSave(settingsToSave, settingRepository);

            List<Author> authorsToSave = new ArrayList<>(authorsMap.values());
            batchSave(authorsToSave, authorRepository);


        } catch (Exception e) {
            throw new RuntimeException("error processing csv", e);
        }

        // process books


        // process many to many relations


    }
}
