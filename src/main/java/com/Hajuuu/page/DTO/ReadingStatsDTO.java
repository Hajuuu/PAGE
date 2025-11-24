package com.Hajuuu.page.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingStatsDTO {
    private long totalBooks;           // 총 읽은 책
    private long totalPages;           // 총 읽은 페이지
    private double avgPagesPerMonth;

}
