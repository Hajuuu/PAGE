package com.Hajuuu.page.repository;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.SearchUserDTO;
import java.util.List;

public interface UserRepositoryCustom {

    List<SearchUserDTO> search(String loginId);

    List<PostDTO> following(List<Integer> followList);
}
