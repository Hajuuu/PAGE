package com.Hajuuu.page.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.Hajuuu.page.domain.NaverUser;
import com.Hajuuu.page.domain.User;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NaverUserRepository naverUserRepository;

    @Autowired
    EntityManager em;

    void init() {
        User user1 = new User();
        user1.createUser("asdf456789", "sdfsafsdfsdafsdds");
        User user2 = new User();
        user2.createUser("zxcv456789", "sadfsffewfew");
        User user3 = new User();
        user3.createUser("qwer7894564", "afsdafwefewfewewf");

        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
    }

    @Test
    void 회원_등록() {
        init();

        Optional<User> result1 = userRepository.findByLoginId("asdf456789");
        Optional<User> result2 = userRepository.findByLoginId("zxcv456789");

        System.out.println(result1.get().getLoginId() + " " + result2.get().getPassword());
        assertThat(result1.get().getPassword()).isEqualTo("sdfsafsdfsdafsdds");
        assertThat(result2.get().getPassword()).isEqualTo("sadfsffewfew");
    }

    @Test
    void 회원_검색() {
        init();
        List<SearchUserDTO> users = userRepository.search("456789");

        assertThat(users.size()).isEqualTo(2);
    }

    @Test
    void 팔로잉() {
        User user1 = new User();
        user1.createUser("asdf456789", "sdfsafsdfsdafsdds");
        User user2 = new User();
        user2.createUser("zxcv456789", "sadfsffewfew");
        User user3 = new User();
        user3.createUser("qwer7894564", "afsdafwefewfewewf");
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        user1.addFollowing(user3.getId());
        user1.addFollowing(user2.getId());

        List<Integer> followingList = user1.getFollowingList();

        System.out.println(followingList.getClass());
        for (int id : followingList) {

            System.out.println("id = " + id);
        }


    }

    @Test
    void 네이버로그인() {

        User user = User.builder().loginId("asdfdasfdsfdsafsd").password("sadfsdfsea234234234").build();

        NaverUser naverUser = NaverUser.builder().user(user).snsId("aaaaa").build();

        userRepository.save(user);
        naverUserRepository.save(naverUser);

        Optional<User> findUser = userRepository.findById(user.getId());
        Optional<NaverUser> findNaverUser = naverUserRepository.findBySnsId("aaaaa");
        assertThat(findUser.get().getId()).isEqualTo(findNaverUser.get().getId());
    }

}