package com.martin.bookstore.repository;

import com.martin.bookstore.entity.Book;
import com.martin.bookstore.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    @Query("""
           select fi
           from FileInfo fi
           join fi.bookFileInfos bfi
           where bfi.book = :book
           and fi.fileName = :fileName
           """)
    Optional<FileInfo> findByBookAndFileName(
            @Param("book") Book book,
            @Param("fileName") String fileName
    );
}
