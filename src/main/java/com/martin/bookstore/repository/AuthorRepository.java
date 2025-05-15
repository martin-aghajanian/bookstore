package com.martin.bookstore.repository;

import com.martin.bookstore.criteria.AuthorSearchCriteria;
import com.martin.bookstore.dto.response.AuthorResponseDto;
import com.martin.bookstore.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("""
    select new com.martin.bookstore.dto.response.AuthorResponseDto(
        a.id,
        a.fullName,
        a.goodReadsAuthor
    )
    from Author a
    where (:#{#criteria.fullName} is null or a.fullName like concat('%', :#{#criteria.fullName}, '%'))
    and (:#{#criteria.goodReadsAuthor} is null or a.goodReadsAuthor = :#{#criteria.goodReadsAuthor})
""")
    Page<AuthorResponseDto> findAll(AuthorSearchCriteria criteria, Pageable pageable);
}
