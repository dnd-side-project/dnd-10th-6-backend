package com.dnd.namuiwiki.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PageableDto<T> {
    private List<T> content;
    private int page;
    private int size;
    private int totalPage;
    private long totalCount;

    public static <T> PageableDto<T> create(Page<T> data) {
        return new PageableDto<>(
                data.getContent(),
                data.getPageable().getPageNumber(),
                data.getSize(),
                data.getTotalPages(),
                data.getTotalElements()
        );
    }

}
