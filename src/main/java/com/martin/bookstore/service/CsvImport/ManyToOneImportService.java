package com.martin.bookstore.service.CsvImport;

import com.martin.bookstore.persistence.entity.*;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ManyToOneImportService {

    private final EditionRepository editionRepository;
    private final LanguageRepository languageRepository;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final SeriesRepository seriesRepository;

    private final CsvUtils csvUtils;


    public ManyToOneImportService(EditionRepository editionRepository, LanguageRepository languageRepository, PublisherRepository publisherRepository, BookFormatRepository bookFormatRepository, SeriesRepository seriesRepository, CsvUtils csvUtils) {
        this.editionRepository = editionRepository;
        this.languageRepository = languageRepository;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.seriesRepository = seriesRepository;
        this.csvUtils = csvUtils;
    }

    public void processCsvFile(MultipartFile file) {

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

    }

}
