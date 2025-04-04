package com.martin.bookstore.controller;


import com.martin.bookstore.service.BookCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/csv-upload")
public class BookCsvController {

    private final BookCsvService bookCsvService;

    @Autowired
    public BookCsvController(BookCsvService bookCsvService) {
        this.bookCsvService = bookCsvService;
    }

}
