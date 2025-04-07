package com.martin.bookstore.service;

import com.martin.bookstore.persistence.repository.BookCharacterRepository;
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
