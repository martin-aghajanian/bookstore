package com.martin.bookstore.controller;

import com.martin.bookstore.service.CsvImport.BookImportService;
import com.martin.bookstore.service.CsvImport.JunctionTablesImportService;
import com.martin.bookstore.service.CsvImport.EntitiesImportService;
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

    private final EntitiesImportService entitiesImportService;
    private final BookImportService bookImportService;
    private final JunctionTablesImportService junctionTablesImportService;

    @Autowired
    public BookCsvController(EntitiesImportService entitiesImportService, BookImportService bookImportService, JunctionTablesImportService junctionTablesImportService) {
        this.entitiesImportService = entitiesImportService;
        this.bookImportService = bookImportService;
        this.junctionTablesImportService = junctionTablesImportService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("file is empty.");
        }
        try {
            entitiesImportService.populateEntityTables(file);
            bookImportService.populateBooksTable(file);
//            junctionTablesImportService.populateJunctionTables(file);
            return ResponseEntity.ok("csv file processed.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error processing CSV file: " + e.getMessage());
        }
    }

}
