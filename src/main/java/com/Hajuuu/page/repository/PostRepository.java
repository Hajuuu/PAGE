package com.Hajuuu.page.repository;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.PostFormDTO;
import com.Hajuuu.page.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {
    List<Post> findAllByBookIdOrderByCreatedTimeDesc(int bookId);

    List<PostDTO> findPostsByFollowingIds(List<Integer> followingIds);

    List<PostFormDTO> search(int bookId);
}
