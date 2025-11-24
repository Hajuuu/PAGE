package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.ReadingStatsDTO;
import com.Hajuuu.page.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadingStatsService {

    private final PostRepository postRepository;

    public ReadingStatsDTO getStats(int userId) {

        long totalBooks = postRepository.countTotalBooks(userId);
        long totalPages = postRepository.sumTotalPages(userId);
        double avgPages = postRepository.avgPagesThisMonth(userId);

        return new ReadingStatsDTO(totalBooks, totalPages, avgPages);
    }
}
