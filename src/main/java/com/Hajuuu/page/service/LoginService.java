package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User login(UserDTO userDTO) {
        User findUser = userRepository.findByLoginId(userDTO.getLoginId());
        if (findUser == null) {
            return null;
        }
        if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), findUser.getPassword())) {
            return null;
        }
        return findUser;
    }
}
