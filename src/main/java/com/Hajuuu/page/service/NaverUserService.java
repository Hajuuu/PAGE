package com.Hajuuu.page.service;

import com.Hajuuu.page.domain.NaverUser;
import com.Hajuuu.page.repository.NaverUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NaverUserService {

    @Autowired
    private final NaverUserRepository naverUserRepository;

    public int join(NaverUser naverUser) {
        naverUserRepository.save(naverUser);
        return naverUser.getId();
    }

}
