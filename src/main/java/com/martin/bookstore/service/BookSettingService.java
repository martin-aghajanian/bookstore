package com.martin.bookstore.service;

import com.martin.bookstore.persistence.repository.BookSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookSettingService {

    private final BookSettingRepository bookSettingRepository;

    @Autowired
    public BookSettingService(BookSettingRepository bookSettingRepository) {
        this.bookSettingRepository = bookSettingRepository;
    }
}
