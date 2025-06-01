package com.martin.bookstore.stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByBookId(Long bookId);

    @Query("""
    select new com.martin.bookstore.stock.LowStockBookResponseDto(
        b.id,
        b.title,
        b.isbn,
        s.quantityAvailable
    )
    from Stock s
    join s.book b
    where s.quantityAvailable < :threshold
""")
    Page<LowStockBookResponseDto> findLowStockBooks(@Param("threshold") int threshold, Pageable pageable);
}
