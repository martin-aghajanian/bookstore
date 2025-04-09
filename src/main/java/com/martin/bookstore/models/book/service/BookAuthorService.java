package com.martin.bookstore.models.book.service;

import com.martin.bookstore.models.book.persistence.repository.BookAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookAuthorService {

    private final BookAuthorRepository bookAuthorRepository;

    @Autowired
    public BookAuthorService(BookAuthorRepository bookAuthorRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
    }
}
