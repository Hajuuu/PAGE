package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.PostFormDTO;
import com.Hajuuu.page.domain.Post;
import com.Hajuuu.page.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Transactional
    public long savePost(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Optional<Post> findOne(int postId) {
        return postRepository.findById(postId);
    }

    public List<PostFormDTO> findPosts(int bookId) {
        return postRepository.search(bookId);
    }
}
