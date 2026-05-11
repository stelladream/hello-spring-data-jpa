package kr.ac.hansung.hellospringdatajpa.service;

import jakarta.persistence.EntityNotFoundException;
import kr.ac.hansung.hellospringdatajpa.entity.User;
import kr.ac.hansung.hellospringdatajpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ── 기본 CRUD ──────────────────────────────────────────────────────────────

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found. id: " + id));
    }

    public User update(Long id, User userDetails) {
        User user = findById(id);
        user.setUsername(userDetails.getUsername());
        user.setAge(userDetails.getAge());
        user.setEmail(userDetails.getEmail());
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // ── [Query Method] ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public List<User> findByUsernameContaining(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ── [@Query] ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<User> findUsersOlderThan(int minAge) {
        return userRepository.findUsersOlderThan(minAge);
    }

    @Transactional(readOnly = true)
    public List<User> searchUsernameNative(String keyword) {
        return userRepository.searchUsernameNative(keyword);
    }
}
