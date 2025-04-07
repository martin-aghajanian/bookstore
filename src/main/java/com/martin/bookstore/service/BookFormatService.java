package com.martin.bookstore.service;

import com.martin.bookstore.persistence.entity.BookFormat;
import com.martin.bookstore.persistence.repository.BookFormatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookFormatService {

    private final BookFormatRepository bookFormatRepository;

    @Autowired
    public BookFormatService(BookFormatRepository bookFormatRepository) {
        this.bookFormatRepository = bookFormatRepository;
    }
}
