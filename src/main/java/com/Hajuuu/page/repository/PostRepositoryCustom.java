package com.Hajuuu.page.repository;

import com.Hajuuu.page.DTO.PostFormDTO;
import java.util.List;

public interface PostRepositoryCustom {

    List<PostFormDTO> search(int bookId);

    long countTotalBooks(int userId);

    long sumTotalPages(int userId);

    double avgPagesThisMonth(int userId);
}
