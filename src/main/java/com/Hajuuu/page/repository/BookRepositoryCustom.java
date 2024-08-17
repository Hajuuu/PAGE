package com.Hajuuu.page.repository;

import com.Hajuuu.page.DTO.SearchBookDTO;
import java.util.List;

public interface BookRepositoryCustom {
    List<SearchBookDTO> search(int userId);
}
