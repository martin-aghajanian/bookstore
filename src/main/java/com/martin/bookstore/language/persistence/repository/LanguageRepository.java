package com.martin.bookstore.language.persistence.repository;

import com.martin.bookstore.language.persistence.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByName(String name);
}
