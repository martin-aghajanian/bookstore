package com.martin.bookstore.service.CsvImport;

import com.martin.bookstore.persistence.entity.*;
import com.martin.bookstore.persistence.entity.Character;
import com.martin.bookstore.persistence.repository.*;
import com.martin.bookstore.service.util.CsvUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EntitiesImportService {

    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final SeriesRepository seriesRepository;

    private final AuthorRepository authorRepository;
    private final AwardRepository awardRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final SettingRepository settingRepository;

    private final CsvUtils csvUtils;


    public EntitiesImportService(EditionRepository editionRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, BookFormatRepository bookFormatRepository, SeriesRepository seriesRepository, AuthorRepository authorRepository, AwardRepository awardRepository, CharacterRepository characterRepository, GenreRepository genreRepository, SettingRepository settingRepository, CsvUtils csvUtils) {
        this.editionRepository = editionRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.seriesRepository = seriesRepository;
        this.authorRepository = authorRepository;
        this.awardRepository = awardRepository;
        this.characterRepository = characterRepository;
        this.genreRepository = genreRepository;
        this.settingRepository = settingRepository;
        this.csvUtils = csvUtils;
    }

    public void populateEntityTables(MultipartFile file) {

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
            Set<String> uniqueAwards = new HashSet<>();
            Set<String> uniqueCharacters = new HashSet<>();
            Set<String> uniqueGenres = new HashSet<>();
            Set<String> uniqueSettings = new HashSet<>();
            Map<String, Author> authorsMap = new HashMap<>();

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

                // authors
                String authorCol = record.get("author").trim();
                if (!authorCol.isEmpty()) {
                    List<String> authorEntries = csvUtils.splitAuthors(authorCol);
                    for (String entry : authorEntries) {
                        boolean isGoodreads = false;
                        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(entry);
                        while (m.find()) {
                            for (String part : m.group(1).split(",")) {
                                if (part.trim().equalsIgnoreCase("Goodreads Author")) {
                                    isGoodreads = true;
                                }
                            }
                        }
                        String name = entry.replaceAll("\\s*\\([^)]*\\)", "").trim();
                        boolean finalIsGoodreads = isGoodreads;
                        authorsMap.compute(name, (k, existing) -> {
                            if (existing == null) {
                                Author author = new Author();
                                author.setFullName(name);
                                author.setGoodReadsAuthor(finalIsGoodreads);
                                return author;
                            } else if (finalIsGoodreads) {
                                existing.setGoodReadsAuthor(true);
                                return existing;
                            }
                            return existing;
                        });
                    }
                }

                // awards
                String awardsCol = record.get("awards").trim();
                if (awardsCol.startsWith("[") && awardsCol.endsWith("]"))
                    awardsCol = awardsCol.substring(1, awardsCol.length() - 1);
                for (String awardStr : awardsCol.split(",")) {
                    String[] parts = awardStr.trim().split("\\(");
                    if (parts.length > 0) {
                        String name = parts[0].replaceAll("[\"']", "").trim();
                        if (!name.isEmpty()) {
                            uniqueAwards.add(name);
                        }
                    }
                }

                // genres
                String genreCol = record.get("genres").trim();
                if (genreCol.startsWith("[") && genreCol.endsWith("]"))
                    genreCol = genreCol.substring(1, genreCol.length() - 1);
                for (String genreStr : genreCol.split(",")) {
                    genreStr = genreStr.replaceAll("[\"']", "").trim();
                    if (!genreStr.isEmpty()) {
                        uniqueGenres.add(genreStr);
                    }
                }

                // characters
                String charCol = record.get("characters").trim();
                if (charCol.startsWith("[") && charCol.endsWith("]"))
                    charCol = charCol.substring(1, charCol.length() - 1);
                for (String charStr : charCol.split(",")) {
                    charStr = charStr.replaceAll("[\"']", "").trim();
                    if (!charStr.isEmpty()) {
                        uniqueCharacters.add(charStr);
                    }
                }

                // settings
                String settingsCol = record.get("setting").trim();
                if (settingsCol.startsWith("[") && settingsCol.endsWith("]"))
                    settingsCol = settingsCol.substring(1, settingsCol.length() - 1);
                for (String settingStr : csvUtils.parseSettings(settingsCol)) {
                    if (!settingStr.isEmpty()) {
                        uniqueSettings.add(settingStr);

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

            csvUtils.batchSave(new ArrayList<>(authorsMap.values()), authorRepository);

            List<Award> awardsToSave = new ArrayList<>();
            for (String name : uniqueAwards) {
                Award award = awardRepository.findByName(name).orElse(new Award());
                award.setName(name);
                awardsToSave.add(award);
            }
            csvUtils.batchSave(awardsToSave, awardRepository);

            List<Genre> genresToSave = new ArrayList<>();
            for (String name : uniqueGenres) {
                Genre genre = genreRepository.findByName(name).orElse(new Genre());
                genre.setName(name);
                genresToSave.add(genre);
            }
            csvUtils.batchSave(genresToSave, genreRepository);

            List<Character> characterstoSave = new ArrayList<>();
            for (String name : uniqueCharacters) {
                Character character = characterRepository.findByName(name).orElse(new Character());
                character.setName(name);
                characterstoSave.add(character);
            }
            csvUtils.batchSave(characterstoSave, characterRepository);

            List<Setting> settingsToSave = new ArrayList<>();
            for (String name : uniqueSettings) {
                Setting setting = settingRepository.findByName(name).orElse(new Setting());
                setting.setName(name);
                settingsToSave.add(setting);
            }
            csvUtils.batchSave(settingsToSave, settingRepository);

        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV for many-to-one relations", e);
        }

    }

}
