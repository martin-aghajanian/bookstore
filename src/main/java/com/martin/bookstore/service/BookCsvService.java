package com.martin.bookstore.service;

import com.martin.bookstore.persistence.entity.*;
import com.martin.bookstore.persistence.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;


@Service
public class BookCsvService {

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


    @Autowired
    public BookCsvService(BookRepository bookRepository, BookAuthorRepository bookAuthorRepository, BookCharacterRepository bookCharacterRepository, BookFormatRepository bookFormatRepository, BookGenreRepository bookGenreRepository, BookSettingRepository bookSettingRepository, CharacterRepository characterRepository, EditionRepository editionRepository, GenreRepository genreRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, SeriesRepository seriesRepository, SettingRepository settingRepository) {
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
    }

    public void processCsvFile(MultipartFile file) {

        // process many to one relations (editions, series, languages, publishers, book_formats)

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            Set<String> uniqueEditions = new HashSet<>();
            Set<String> uniqueLanguages = new HashSet<>();
            Set<String> uniquePublishers = new HashSet<>();
            Set<String> uniqueBookFormats = new HashSet<>();
            Set<String> uniqueSeries = new HashSet<>();

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

            }

            for (String editionName : uniqueEditions) {
                Edition edition = editionRepository.findByName(editionName).orElse(new Edition());
                edition.setName(editionName);
                editionRepository.save(edition);
            }

            for (String languageName : uniqueLanguages) {
                Language language = languageRepository.findByName(languageName).orElse(new Language());
                language.setName(languageName);
                languageRepository.save(language);
            }

            for (String publisherName : uniquePublishers) {
                Publisher publisher = publisherRepository.findByName(publisherName).orElse(new Publisher());
                publisher.setName(publisherName);
                publisherRepository.save(publisher);
            }

            for (String bookFormat : uniqueBookFormats) {
                BookFormat format = bookFormatRepository.findByFormat(bookFormat).orElse(new BookFormat());
                format.setFormat(bookFormat);
                bookFormatRepository.save(format);
            }

            for (String seriesName : uniqueSeries) {
                Series series = seriesRepository.findByName(seriesName).orElse(new Series());
                series.setName(seriesName);
                seriesRepository.save(series);
            }

        } catch (Exception e) {
            throw new RuntimeException("error processing csv", e);
        }

        // process books


        // process many to many relations


    }
}
