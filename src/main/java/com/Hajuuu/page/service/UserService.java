package com.Hajuuu.page.service;

import com.Hajuuu.page.DTO.PostDTO;
import com.Hajuuu.page.DTO.SearchUserDTO;
import com.Hajuuu.page.domain.Book;
import com.Hajuuu.page.domain.User;
import com.Hajuuu.page.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public int join(User user) {
        validateDuplicateUser(user.getLoginId());
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(String loginId) {
        Optional<User> findUsers = userRepository.findByLoginId(loginId);
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
        return userRepository.findByLoginId(loginId).get().getBooks();
    }

    public void addFollowing(User user, int id) {
        user.addFollowing(id);
        Optional<User> followingUser = userRepository.findById(id);
        followingUser.get().addFollower(user.getId());
    }

    public List<PostDTO> findFollowingUsersPost(User user) {
        List<Integer> followingList = user.getFollowingList();
        List<PostDTO> following = userRepository.following(followingList);
        return following;
    }
}
