package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.SearchUserDTO;
import com.Hajuuu.page.DTO.SettingDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.PostRepository;
import com.Hajuuu.page.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;


    public void securityJoin(UserDTO userDTO) {
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            return;
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO.toEntity());
    }

    private void validateDuplicateUser(String email) {
        Optional<User> findUsers = userRepository.findByEmail(email);
        if (findUsers.isPresent()) {
            throw new IllegalStateException("사용할 수 없는 이름입니다.");
        }

    }

    public List<SearchUserDTO> search(String loginId) {
        return userRepository.search(loginId);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(int userId) {
        return userRepository.findById(userId);
    }

    public List<Book> findBooks(String loginId) {
        return userRepository.findByLoginId(loginId)
                .map(User::getBooks)
                .orElse(Collections.emptyList());
    }

    @Transactional
    public void addFollowing(User user, int id) {
        user.addFollowing(id);

        User followingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("유저 없음"));

        followingUser.addFollower(user.getId());

        userRepository.save(user);           // DB에 반영
        userRepository.save(followingUser);  // DB에 반영
    }


    public List<PostDTO> findFollowingUsersPost(User loginUser) {
        List<Integer> followingIds = loginUser.getFollowings();
        if (followingIds.isEmpty()) {
            return Collections.emptyList();
        }
        return postRepository.findPostsByFollowingIds(followingIds);
    }

    public User findByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser.orElse(null);
    }

    public User findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }

    private boolean isPasswordFormatValid(SettingDTO settingDTO) {
        return settingDTO.getChangePassword() != null && settingDTO.getChangePassword().length() >= 8;
    }

    private boolean isPasswordValid(SettingDTO settingDTO) {
        if (!isPasswordFormatValid(settingDTO)) {
            return false;
        }
        return userRepository.findByLoginId(settingDTO.getLoginId())
                .map(user -> passwordEncoder.matches(settingDTO.getCheckPassword(), user.getPassword()))
                .orElse(false);
    }


    @Transactional
    public void changePassword(SettingDTO settingDTO) {
        User findUser = userRepository.findByLoginId(settingDTO.getLoginId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + settingDTO.getLoginId()));
        if (!isPasswordValid(settingDTO)) {
            findUser.updatePassword(passwordEncoder.encode(settingDTO.getPassword()));
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        if (passwordEncoder.matches(settingDTO.getChangePassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 달라야 합니다.");
        }
        findUser.updatePassword(passwordEncoder.encode(passwordEncoder.encode(settingDTO.getChangePassword())));

    }
}
