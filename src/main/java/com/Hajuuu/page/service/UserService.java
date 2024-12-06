package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.SearchUserDTO;
import com.Hajuuu.page.DTO.SettingDTO;
import com.Hajuuu.page.DTO.UserDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void securityJoin(UserDTO userDTO) {
        if (userRepository.existsByLoginId(userDTO.getLoginId())) {
            return;
        }
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userDTO.toEntity());
    }

    private void validateDuplicateUser(String email) {
        Optional<User> findUsers = userRepository.findByEmail(email);
        if (!findUsers.isEmpty()) {
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
        return userRepository.findByLoginId(loginId).getBooks();
    }

    public void addFollowing(User user, int id) {
        user.addFollowing(id);
        Optional<User> followingUser = userRepository.findById(id);
        followingUser.get().addFollower(user.getId());
    }

    public List<PostDTO> findFollowingUsersPost(User user) {
        if (user == null) {
            return null;
        }
        List<Integer> followingList = user.getFollowingList();

        List<PostDTO> following = userRepository.following(followingList);
        return following;
    }

    public User findByEmail(String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        return findUser.orElse(null);
    }

    public User findByLoginId(String loginId) {
        User findUser = userRepository.findByLoginId(loginId);
        return findUser;
    }

    public boolean checkPassword(SettingDTO settingDTO) {
        User findUser = userRepository.findByLoginId(settingDTO.getLoginId());
        if (!bCryptPasswordEncoder.matches(settingDTO.getCheckPassword(), findUser.getPassword())) {
            return false;
        }
        return true;
    }
}
