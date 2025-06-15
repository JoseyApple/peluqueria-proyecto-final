package org.example.peluqueria.infraestructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public final class PageOutDto<T> {

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private long totalPages;
    private List<T> content;

    public PageOutDto(int pageNumber, int pageSize, long totalElements, long totalPages, List<T> content) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
}
