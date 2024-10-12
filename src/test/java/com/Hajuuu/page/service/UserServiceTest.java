package com.Hajuuu.page.service;

import com.Hajuuu.page.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Test
    void 동시성_테스트() throws InterruptedException {
        String loginId = "abc123";
        User user = new User();
        user.createUser(loginId, "132");

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                userService.join(user);
                countDownLatch.countDown();
                try {
                    Thread.sleep(3000);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            });

        }
        countDownLatch.await();


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
        userService.addFollowing(user1, user2.getId());

        List<Integer> followingList = user1.getFollowingList();
        List<Integer> followerList = user2.getFollowerList();

        for (int aLong : followerList) {
            System.out.println("aLong = " + aLong);
        }

        for (int aLong : followingList) {
            System.out.println("aLong = " + aLong);
        }
    }
}