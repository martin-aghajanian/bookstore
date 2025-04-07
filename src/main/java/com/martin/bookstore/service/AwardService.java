package com.martin.bookstore.service;

import com.martin.bookstore.persistence.repository.AwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AwardService {

    private final AwardRepository awardRepository;

    @Autowired
    public AwardService(AwardRepository awardRepository) {
        this.awardRepository = awardRepository;
    }
}
