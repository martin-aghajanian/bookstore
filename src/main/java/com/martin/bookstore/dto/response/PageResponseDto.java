package com.martin.bookstore.dto.response;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    public static <T> PageResponseDto<T> from(Page<T> page) {
        PageResponseDto<T> pageResponseDto = new PageResponseDto<>();

        pageResponseDto.setContent(page.getContent());
        pageResponseDto.setPageSize(page.getPageable().getPageSize());
        pageResponseDto.setPageNumber(page.getPageable().getPageNumber());
        pageResponseDto.setTotalPages(page.getTotalPages());
        pageResponseDto.setTotalElements(page.getTotalElements());

        return pageResponseDto;
    }

    public static <T> PageResponseDto<T> empty(int page, int size) {
        return PageResponseDto.<T>builder()
                .content(Collections.emptyList())
                .pageNumber(page)
                .pageSize(size)
                .totalElements(0L)
                .build();
    }
}
