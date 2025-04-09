package com.martin.bookstore.models.book.service;

import com.martin.bookstore.models.book.persistence.repository.BookGenreRepository;
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
