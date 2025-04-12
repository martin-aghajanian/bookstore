package com.martin.bookstore.csvimport.controller;

import com.martin.bookstore.csvimport.service.CsvImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/csv-upload")
public class CsvImportController {

    private final CsvImportService csvImportService;

    public CsvImportController(CsvImportService csvImportService) {
        this.csvImportService = csvImportService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("file is empty.");
        }
        try {
            csvImportService.importCsv(file);
            return ResponseEntity.ok("csv file processed.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error processing CSV file: " + e.getMessage());
        }
    }

}
