package com.martin.bookstore.models.book.service;

import com.martin.bookstore.models.book.persistence.repository.BookCharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookCharacterService {

    private final BookCharacterRepository bookCharacterRepository;

    @Autowired
    public BookCharacterService(BookCharacterRepository bookCharacterRepository) {
        this.bookCharacterRepository = bookCharacterRepository;
    }
}
