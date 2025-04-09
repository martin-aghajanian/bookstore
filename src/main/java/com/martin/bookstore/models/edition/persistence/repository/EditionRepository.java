package com.martin.bookstore.models.edition.persistence.repository;

import com.martin.bookstore.models.edition.persistence.entity.Edition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditionRepository extends JpaRepository<Edition, Long> {
    Optional<Edition> findByName(String name);
}
