package com.Hajuuu.page.repository;

import com.Hajuuu.page.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {

    User findByLoginId(String loginId);

    Boolean existsByLoginId(String loginId);

    Optional<User> findByEmail(String email);
}
