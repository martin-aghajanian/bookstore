package com.martin.bookstore.openai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PromptTemplate {

    DEFAULT_PROMPT("""
            You are a strict JSON generator. The user will give you a prompt for book suggestions.
            You MUST respond with ONLY a JSON object in this format and NOTHING else:
           \s
            {
              "books": [
                "Book Title 1",
                "Book Title 2",
                "Book Title 3",
                ...
                "Book Title 15"
              ]
            }
           \s
            Do not include explanations, introductions, markdown, or author names. Return only valid JSON. In case you cannot generate books, return empty array in the same format.
            Do not use markdown formatting like ```json or ``` in your response. Only return raw JSON.
           \s
            User input:\s
""");

    @Getter
    private final String prompt;
}
