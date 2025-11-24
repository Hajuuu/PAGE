package com.Hajuuu.page.repository;

import com.Hajuuu.page.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {

    Optional<User> findByLoginId(String loginId);

    Boolean existsByLoginId(String loginId);

    Optional<User> findByEmail(String email);
}
