package com.martin.bookstore.repository;

import com.martin.bookstore.entity.BookFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileInfoRepository extends JpaRepository<BookFileInfo, Long> {
}
