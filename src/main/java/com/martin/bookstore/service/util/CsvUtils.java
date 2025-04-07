package com.martin.bookstore.service.util;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvUtils {

    private static final int BATCH_SIZE = 5000;

    public static <T> void batchSave(List<T> entities, JpaRepository<T, ?> repository) {
        for (int i = 0; i < entities.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, entities.size());
            repository.saveAll(entities.subList(i, endIndex));
        }
    }

    public static List<String> parseSettings(String input) {
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

    public static List<String> splitAuthors(String authorsColumn) {
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
}
