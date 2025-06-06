package com.martin.bookstore.book.persistence.repository;

import com.martin.bookstore.book.persistence.entity.BookSetting;
import com.martin.bookstore.book.persistence.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookSettingRepository extends JpaRepository<BookSetting, Long> {
    void deleteAllByBook(Book book);
}
