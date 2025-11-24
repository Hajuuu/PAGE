package com.Hajuuu.page.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    PostRepository postRepository;

//    @Test
//    void searchTest() {
//        User user = new User();
//        user.createUser("asdf456789", "asdfdsfdf");
//        Book book1 = new Book();
//        book1.createBook(user, "abc", "asdf", 155L, "asdf", "12345", BookState.READING);
//        Book book2 = new Book();
//        book2.createBook(user, "sfadfsdfdffds", "sdfsdfsd", 255L, "dfsfsfa", "151255122", BookState.COMPLETE);
//        Post post1 = new Post();
//        post1.createPost(book1, user, "asdfsfdsfasdfasd");
//        Post post2 = new Post();
//        post2.createPost(book2, user, "qwrwerrwerewrwere");
//        em.persist(user);
//        em.persist(book1);
//        em.persist(book2);
//        em.persist(post1);
//        em.persist(post2);
//
//        List<PostFormDTO> result1 = postRepository.search(book1.getId());
//        List<PostFormDTO> result2 = postRepository.search(book2.getId());
//        for (PostFormDTO postFormDTO : result1) {
//            System.out.println("postFormDTO = " + postFormDTO);
//        }
//        for (PostFormDTO postFormDTO : result2) {
//            System.out.println("postFormDTO = " + postFormDTO);
//        }
//    }

}