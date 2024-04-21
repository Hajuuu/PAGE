package com.Hajuuu.page.service;

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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    public Long join(User user) {
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

    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findOne(Long userId) {
        return userRepository.findById(userId);
    }

    public List<Book> findBooks(String loginId) {
        return findByLoginId(loginId).get().getBooks();
    }

}
