package com.Hajuuu.page.service;

import com.Hajuuu.page.domain.Post;
import com.Hajuuu.page.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public long savePost(Post post) {
        postRepository.save(post);
        return post.getId();
    }

    public Post findOne(Long postId) {
        return postRepository.findOne(postId);
    }

    public List<Post> findPosts() {
        return postRepository.findAll();
    }
}
