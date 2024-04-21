package com.Hajuuu.page;

import com.Hajuuu.page.domain.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class InitUser {

    private final InitUserService initUserService;

    @PostConstruct
    public void init() {
        initUserService.init();
    }

    @Component
    static class InitUserService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            User user1 = new User();
            User user2 = new User();
            user1.createUser("qwer123456", "qwer456789");
            user2.createUser("asdf123456", "asdf789456");
            em.persist(user1);
            em.persist(user2);
        }
    }
}
