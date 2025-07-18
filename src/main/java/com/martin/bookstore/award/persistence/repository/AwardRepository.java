package com.martin.bookstore.award.persistence.repository;

import com.martin.bookstore.award.dto.AwardResponseDto;
import com.martin.bookstore.award.criteria.AwardSearchCriteria;
import com.martin.bookstore.award.persistence.entity.Award;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {

    @Query("""
        select distinct new com.martin.bookstore.award.dto.AwardResponseDto(
            a.id,
            a.name
        )
        from Award a
            left join a.bookAwards ba
        where (:#{#criteria.name} is null or lower(a.name) like concat('%', lower(:#{#criteria.name}), '%'))
            and coalesce(:#{#criteria.year}, ba.year) = ba.year
    """)
    Page<AwardResponseDto> findAll(AwardSearchCriteria criteria, Pageable pageable);

}
