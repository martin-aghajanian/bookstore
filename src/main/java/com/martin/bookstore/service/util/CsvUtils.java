package com.martin.bookstore.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
