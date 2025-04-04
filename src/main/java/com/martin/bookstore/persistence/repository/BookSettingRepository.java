package com.martin.bookstore.persistence.repository;

import com.martin.bookstore.persistence.entity.BookSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSettingRepository extends JpaRepository<BookSetting, Long> {
}
