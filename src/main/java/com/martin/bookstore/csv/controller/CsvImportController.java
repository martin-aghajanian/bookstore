package com.martin.bookstore.csv.controller;

import com.martin.bookstore.csv.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/csv-upload")
@RequiredArgsConstructor
public class CsvImportController {

    private final CsvImportService csvImportService;

    @PreAuthorize("hasAuthority('data:import')")
    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("file is empty.");
        }
        try {
            csvImportService.importCsv(file);
            return ResponseEntity.ok("csv file processed.");

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("error processing CSV file: " + e.getMessage());
        }
    }

}
