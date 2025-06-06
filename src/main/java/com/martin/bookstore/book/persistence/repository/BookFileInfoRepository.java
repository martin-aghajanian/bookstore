package com.martin.bookstore.book.persistence.repository;

import com.martin.bookstore.book.persistence.entity.BookFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileInfoRepository extends JpaRepository<BookFileInfo, Long> {
}
