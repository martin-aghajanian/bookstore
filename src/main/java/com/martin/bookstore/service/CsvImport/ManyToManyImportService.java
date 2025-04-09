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
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ManyToManyImportService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AwardRepository awardRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final SettingRepository settingRepository;

    private final BookAuthorRepository bookAuthorRepository;
    private final BookAwardRepository bookAwardRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final BookGenreRepository bookGenreRepository;
    private final BookSettingRepository bookSettingRepository;

    private final CsvUtils csvUtils;

    public ManyToManyImportService(BookRepository bookRepository, AuthorRepository authorRepository,
                                   AwardRepository awardRepository, CharacterRepository characterRepository,
                                   GenreRepository genreRepository, SettingRepository settingRepository,
                                   BookAuthorRepository bookAuthorRepository, BookAwardRepository bookAwardRepository,
                                   BookCharacterRepository bookCharacterRepository, BookGenreRepository bookGenreRepository,
                                   BookSettingRepository bookSettingRepository, CsvUtils csvUtils) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.awardRepository = awardRepository;
        this.characterRepository = characterRepository;
        this.genreRepository = genreRepository;
        this.settingRepository = settingRepository;
        this.bookAuthorRepository = bookAuthorRepository;
        this.bookAwardRepository = bookAwardRepository;
        this.bookCharacterRepository = bookCharacterRepository;
        this.bookGenreRepository = bookGenreRepository;
        this.bookSettingRepository = bookSettingRepository;
        this.csvUtils = csvUtils;
    }

    public void populateEntityTables(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            Set<String> uniqueAwards = new HashSet<>();
            Set<String> uniqueCharacters = new HashSet<>();
            Set<String> uniqueGenres = new HashSet<>();
            Set<String> uniqueSettings = new HashSet<>();
            Map<String, Author> authorsMap = new HashMap<>();

            for (CSVRecord record : parser) {

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
            throw new RuntimeException("Failed to populate entity tables", e);
        }
    }


    public void populateJunctionTables(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<BookAuthor> bookAuthors = new ArrayList<>();
            List<BookAward> bookAwards = new ArrayList<>();
            List<BookGenre> bookGenres = new ArrayList<>();
            List<BookCharacter> bookCharacters = new ArrayList<>();
            List<BookSetting> bookSettings = new ArrayList<>();

            for (CSVRecord record : parser) {
                String isbnStr = record.get("isbn").trim();
                Long bookIsbn = Long.parseLong(isbnStr);
                Book book = bookRepository.findByIsbn(bookIsbn).orElse(null);
                System.out.println(book.getTitle());
                if (book == null) {
                    continue;
                }

                // authors
//                String authorCol = record.get("author").trim();
//                if (!authorCol.isEmpty()) {
//                    List<String> authorEntries = csvUtils.splitAuthors(authorCol);
//                    for (String entry : authorEntries) {
//                        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(entry);
//                        String contribution = m.find() ? m.group(1).trim() : "Author";
//                        String name = entry.replaceAll("\\s*\\([^)]*\\)", "").trim();
//                        Author author = authorRepository.findByFullName(name).orElse(null);
//                        if (author != null) {
//                            BookAuthor ba = new BookAuthor();
//                            ba.setBook(book);
//                            ba.setAuthor(author);
//                            ba.setContribution(contribution);
//                            bookAuthors.add(ba);
//                        }
//                    }
//                }

                // awards
                String awardsColumn = record.get("awards").trim();
                if (awardsColumn.startsWith("[") && awardsColumn.endsWith("]"))
                    awardsColumn = awardsColumn.substring(1, awardsColumn.length() - 1);
                for (String awardStr : awardsColumn.split(",")) {
                    String[] parts = awardStr.trim().split("\\(");
                    if (parts.length > 0) {
                        String awardName = parts[0].replaceAll("[\"']", "").trim();
                        String yearStr = parts[1].replace(")", "").trim();

                        try {
                            int year = Integer.parseInt(yearStr);
                            Award award = awardRepository.findByName(awardName).orElse(null);
                            if (award != null) {
                                BookAward ba = new BookAward();
                                ba.setBook(book);
                                ba.setAward(award);
                                ba.setYear(LocalDate.of(year, 1, 1));
                                bookAwards.add(ba);
                            }
                        } catch (NumberFormatException ignored) {}

                    }
                }

                // Genres
                String genreCol = record.get("genres").trim();
                if (genreCol.startsWith("[") && genreCol.endsWith("]"))
                    genreCol = genreCol.substring(1, genreCol.length() - 1);
                for (String genreStr : genreCol.split(",")) {
                    genreStr = genreStr.replaceAll("[\"']", "").trim();
                    Genre genre = genreRepository.findByName(genreStr).orElse(null);
                    if (genre != null) {
                        BookGenre bg = new BookGenre();
                        bg.setBook(book);
                        bg.setGenre(genre);
                        bookGenres.add(bg);
                    }
                }

                // characters
                String charCol = record.get("characters").trim();
                if (charCol.startsWith("[") && charCol.endsWith("]"))
                    charCol = charCol.substring(1, charCol.length() - 1);
                for (String charStr : charCol.split(",")) {
                    charStr = charStr.replaceAll("[\"']", "").trim();
                    Character character = characterRepository.findByName(charStr).orElse(null);
                    if (character != null) {
                        BookCharacter bc = new BookCharacter();
                        bc.setBook(book);
                        bc.setCharacter(character);
                        bookCharacters.add(bc);
                    }
                }

                // settings
                String settingsCol = record.get("setting").trim();
                if (settingsCol.startsWith("[") && settingsCol.endsWith("]")) {
                    settingsCol = settingsCol.substring(1, settingsCol.length() - 1);
                }

                for (String settingStr : csvUtils.parseSettings(settingsCol)) {

                    if (!settingStr.isEmpty()) {
                        Setting setting = settingRepository.findByName(settingStr).orElse(null);
                        if (setting != null) {
                            BookSetting bs = new BookSetting();
                            bs.setBook(book);
                            bs.setSetting(setting);
                            bookSettings.add(bs);
                        }
                    }


                }
            }

//            csvUtils.batchSave(bookAuthors, bookAuthorRepository);
            csvUtils.batchSave(bookAwards, bookAwardRepository);
            csvUtils.batchSave(bookGenres, bookGenreRepository);
            csvUtils.batchSave(bookCharacters, bookCharacterRepository);
            csvUtils.batchSave(bookSettings, bookSettingRepository);


        } catch (Exception e) {
            throw new RuntimeException("failed to populate junction tables", e);
        }
    }
}
