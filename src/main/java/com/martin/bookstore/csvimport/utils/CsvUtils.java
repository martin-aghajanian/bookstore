package com.martin.bookstore.csvimport.utils;

import com.martin.bookstore.csvimport.enums.CsvHeader;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
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

    @Value("${batch.size:5000}")
    private int batchSize;


    public <T> void batchSave(List<T> entities, JpaRepository<T, ?> repository) {
        for (int i = 0; i < entities.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, entities.size());
            repository.saveAll(entities.subList(i, endIndex));
        }
    }

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
        if (current.length() > 0) {
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
                throw new RuntimeException("csv file is missing required header: " + header);
            }
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

    public void tryParseInt(String value, IntConsumer setter) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                setter.accept(Integer.parseInt(value.trim()));
            }
        } catch (NumberFormatException ignored) {
        }
    }

    public void tryParseLong(String value, LongConsumer setter) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                setter.accept(Long.parseLong(value.trim()));
            }
        } catch (NumberFormatException ignored) {
        }
    }

    public void tryParseDouble(String value, DoubleConsumer setter) {
        try {
            if (value != null && !value.trim().isEmpty()) {
                setter.accept(Double.parseDouble(value.trim()));
            }
        } catch (NumberFormatException ignored) {
        }
    }


}
