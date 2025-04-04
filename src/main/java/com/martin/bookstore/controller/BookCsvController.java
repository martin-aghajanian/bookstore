package com.martin.bookstore.controller;


import com.martin.bookstore.service.BookCsvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/csv-upload")
public class BookCsvController {

    private final BookCsvService bookCsvService;

    @Autowired
    public BookCsvController(BookCsvService bookCsvService) {
        this.bookCsvService = bookCsvService;
    }

    @PostMapping
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {

            return ResponseEntity.badRequest().body("file is empty.");
        }
        try {
            long start = System.currentTimeMillis();
            bookCsvService.processCsvFile(file);
            long total = System.currentTimeMillis() - start;
            return ResponseEntity.ok("csv file processed in " + total + " ms.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error processing CSV file: " + e.getMessage());
        }
    }

}
