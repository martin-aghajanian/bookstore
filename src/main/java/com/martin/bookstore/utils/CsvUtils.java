package com.martin.bookstore.utils;

import com.martin.bookstore.enums.CsvHeader;
import com.martin.bookstore.exception.CsvProcessingException;
import com.martin.bookstore.entity.*;
import com.martin.bookstore.entity.Character;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CsvUtils {

    public List<String> parseSettings(String input) {
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

    public List<String> splitAuthors(String authorsColumn) {
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

            if (ch == ',' && parenthesesLevel == 0) {
                authors.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        if (!current.isEmpty()) {
            authors.add(current.toString().trim());
        }
        return authors;
    }

    public void validateCsvHeaders(CSVParser parser) {
        Set<String> requiredHeaders = Arrays.stream(CsvHeader.values())
                .map(CsvHeader::value)
                .collect(Collectors.toSet());

        for (String header : requiredHeaders) {
            if (!parser.getHeaderMap().containsKey(header)) {
                throw new CsvProcessingException("csv file is missing required header: " + header);
            }
        }
    }

    public void validateRequiredFields(int recordCount, String title, String isbnStr, String publishDateStr) {
        if (title.isEmpty() || !isbnStr.matches("\\d{13}") || publishDateStr.isEmpty()) {
            throw new CsvProcessingException("Record #" + recordCount + " is missing required fields or has invalid ISBN");
        }
    }

    public LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;

        dateStr = dateStr.trim().replaceAll("(?<=\\d)(st|nd|rd|th)", "");
        List<DateTimeFormatter> formatters = new ArrayList<>();

        if (dateStr.contains("/")) {
            formatters.add(DateTimeFormatter.ofPattern("MM/dd/yy"));
        } else {
            formatters.add(DateTimeFormatter.ISO_LOCAL_DATE);
        }

        formatters.add(DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH));
        formatters.add(new DateTimeFormatterBuilder()
                .appendPattern("MMMM yyyy")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter(Locale.ENGLISH));
        formatters.add(new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter(Locale.ENGLISH));

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    public void tryParseInt(String value, IntConsumer setter, int recordCount, String fieldName) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                setter.accept(Integer.parseInt(value.trim()));
            }
        } catch (NumberFormatException e) {
            throw new CsvProcessingException("Record #" + recordCount + ": invalid integer value for field '" + fieldName + "': " + value, e);
        }
    }

    public void tryParseLong(String value, LongConsumer setter, int recordCount, String fieldName) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                setter.accept(Long.parseLong(value.trim()));
            }
        } catch (NumberFormatException e) {
            throw new CsvProcessingException("Record #" + recordCount + ": invalid long value for field '" + fieldName + "': " + value, e);
        }
    }

    public void tryParseDouble(String value, DoubleConsumer setter, int recordCount, String fieldName) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                setter.accept(Double.parseDouble(value.trim()));
            }
        } catch (NumberFormatException e) {
            throw new CsvProcessingException("Record #" + recordCount + ": invalid double value for field '" + fieldName + "': " + value, e);
        }
    }



    public void resolveEdition(Book book, String editionName,
                               Map<String, Edition> editionDbMap,
                               Map<String, Edition> editions) {
        if (!editionName.isEmpty()) {
            Edition edition = editionDbMap.get(editionName);
            if (edition == null) {
                edition = editions.computeIfAbsent(editionName, name -> {
                    Edition e = new Edition();
                    e.setName(name);
                    return e;
                });
            }
            book.setEdition(edition);
        }
    }

    public void resolveLanguage(Book book, String languageName,
                                Map<String, Language> languageDbMap,
                                Map<String, Language> languages) {
        if (!languageName.isEmpty()) {
            Language language = languageDbMap.get(languageName);
            if (language == null) {
                language = languages.computeIfAbsent(languageName, name -> {
                    Language l = new Language();
                    l.setName(name);
                    return l;
                });
            }
            book.setLanguage(language);
        }
    }

    public void resolvePublisher(Book book, String publisherName,
                                 Map<String, Publisher> publisherDbMap,
                                 Map<String, Publisher> publishers) {
        if (!publisherName.isEmpty()) {
            Publisher publisher = publisherDbMap.get(publisherName);
            if (publisher == null) {
                publisher = publishers.computeIfAbsent(publisherName, name -> {
                    Publisher p = new Publisher();
                    p.setName(name);
                    return p;
                });
            }
            book.setPublisher(publisher);
        }
    }

    public void resolveFormat(Book book, String formatName,
                              Map<String, Format> formatDbMap,
                              Map<String, Format> formats) {
        if (!formatName.isEmpty()) {
            Format format = formatDbMap.get(formatName);
            if (format == null) {
                format = formats.computeIfAbsent(formatName, name -> {
                    Format f = new Format();
                    f.setFormat(name);
                    return f;
                });
            }
            book.setFormat(format);
        }
    }

    public void resolveSeries(Book book, String seriesField,
                              Map<String, Series> seriesDbMap,
                              Map<String, Series> seriesSet) {
        if (!seriesField.isEmpty()) {
            int hashIndex = seriesField.indexOf("#");
            String seriesName = (hashIndex != -1)
                    ? seriesField.substring(0, hashIndex).trim()
                    : seriesField.trim();

            if (!seriesName.isEmpty()) {
                Series series = seriesDbMap.get(seriesName);
                if (series == null) {
                    series = seriesSet.computeIfAbsent(seriesName, name -> {
                        Series s = new Series();
                        s.setName(name);
                        return s;
                    });
                }
                book.setSeries(series);
            }
        }
    }

    public void resolveAuthors(Book book, String authorCol,
                               Map<String, Author> authorDbMap,
                               Map<String, Author> authorsMap,
                               Queue<BookAuthor> bookAuthors) {

        if (!authorCol.isEmpty()) {
            List<String> authorEntries = splitAuthors(authorCol);
            for (String entry : authorEntries) {
                boolean isGoodreads = false;
                Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(entry);
                String contribution = m.find() ? m.group(1).trim() : "Author";

                while (m.find()) {
                    for (String part : m.group(1).split(",")) {
                        if (part.trim().equalsIgnoreCase("Goodreads Author")) {
                            isGoodreads = true;
                            break;
                        }
                    }
                }

                String name = entry.replaceAll("\\s*\\([^)]*\\)", "").trim();
                boolean finalIsGoodreads = isGoodreads;

                Author author = authorDbMap.get(name);
                if (author == null) {
                    author = authorsMap.computeIfAbsent(name, key -> {
                        Author a = new Author();
                        a.setFullName(key);
                        a.setGoodReadsAuthor(finalIsGoodreads);
                        return a;
                    });
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
    }


    public void resolveAwards(Book book, String awardsCol,
                              Map<String, Award> awardDbMap,
                              Map<String, Award> awards,
                              Queue<BookAward> bookAwards) {

        if (awardsCol.startsWith("[") && awardsCol.endsWith("]"))
            awardsCol = awardsCol.substring(1, awardsCol.length() - 1);

        for (String awardStr : awardsCol.split(",")) {
            awardStr = awardStr.trim().replaceAll("[\"']", "");
            int lastParen = awardStr.lastIndexOf(" (");
            if (lastParen != -1) {
                String name = awardStr.substring(0, lastParen).trim();
                String yearStr = awardStr.substring(lastParen + 2).replace(")", "").trim();

                Award award = awardDbMap.get(name);
                if (award == null) {
                    award = awards.computeIfAbsent(name, key -> {
                        Award a = new Award();
                        a.setName(key);
                        return a;
                    });
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
    }

    public void resolveGenres(Book book, String genreCol,
                              Map<String, Genre> genreDbMap,
                              Map<String, Genre> genres,
                              Queue<BookGenre> bookGenres) {

        if (genreCol.startsWith("[") && genreCol.endsWith("]"))
            genreCol = genreCol.substring(1, genreCol.length() - 1);

        for (String g : genreCol.split(",")) {
            g = g.replaceAll("[\"']", "").trim();
            if (!g.isEmpty()) {
                Genre genre = genreDbMap.get(g);
                if (genre == null) {
                    genre = genres.computeIfAbsent(g, key -> {
                        Genre genre1 = new Genre();
                        genre1.setName(key);
                        return genre1;
                    });
                }
                BookGenre bg = new BookGenre();
                bg.setBook(book);
                bg.setGenre(genre);
                bookGenres.add(bg);
            }
        }
    }

    public void resolveCharacters(Book book, String charCol,
                                  Map<String, Character> characterDbMap,
                                  Map<String, Character> characters,
                                  Queue<BookCharacter> bookCharacters) {

        if (charCol.startsWith("[") && charCol.endsWith("]"))
            charCol = charCol.substring(1, charCol.length() - 1);

        for (String c : charCol.split(",")) {
            c = c.replaceAll("[\"']", "").trim();
            if (!c.isEmpty()) {
                Character character = characterDbMap.get(c);
                if (character == null) {
                    character = characters.computeIfAbsent(c, key -> {
                        Character ch = new Character();
                        ch.setName(key);
                        return ch;
                    });
                }
                BookCharacter bc = new BookCharacter();
                bc.setBook(book);
                bc.setCharacter(character);
                bookCharacters.add(bc);
            }
        }
    }

    public void resolveSettings(Book book, String settingCol,
                                Map<String, Setting> settingDbMap,
                                Map<String, Setting> settings,
                                Queue<BookSetting> bookSettings) {

        if (settingCol.startsWith("[") && settingCol.endsWith("]"))
            settingCol = settingCol.substring(1, settingCol.length() - 1);

        for (String s : parseSettings(settingCol)) {
            if (!s.isEmpty()) {
                Setting setting = settingDbMap.get(s);
                if (setting == null) {
                    setting = settings.computeIfAbsent(s, key -> {
                        Setting st = new Setting();
                        st.setName(key);
                        return st;
                    });
                }
                BookSetting bs = new BookSetting();
                bs.setBook(book);
                bs.setSetting(setting);
                bookSettings.add(bs);
            }
        }
    }
}
