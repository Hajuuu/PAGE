package com.Hajuuu.page.repository;

import com.Hajuuu.page.domain.NaverUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NaverUserRepository extends JpaRepository<NaverUser, Integer> {

    Optional<NaverUser> findBySnsId(String snsId);
}
