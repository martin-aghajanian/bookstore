package com.martin.bookstore.service;

import com.martin.bookstore.persistence.repository.BookGenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;

    @Autowired
    public BookGenreService(BookGenreRepository bookGenreRepository) {
        this.bookGenreRepository = bookGenreRepository;
    }
}
