package com.martin.bookstore.service;

import com.martin.bookstore.persistence.repository.EditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditionService {

    private final EditionRepository editionRepository;

    @Autowired
    public EditionService(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }
}
