package kr.ac.hansung.hellospringdatajpa.repository;

import kr.ac.hansung.hellospringdatajpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ── 기본 CRUD (JpaRepository 상속) ────────────────────────────────────────
    // save / findAll / findById / count / deleteById

    // ── [Query Method] ────────────────────────────────────────────────────────

    List<User> findByUsername(String username);

    List<User> findByUsernameContaining(String keyword);

    Optional<User> findByEmail(String email);

    // ── [@Query] ──────────────────────────────────────────────────────────────

    // JPQL — 이름 기반 파라미터(@Param)
    @Query("SELECT u FROM User u WHERE u.age >= :minAge ORDER BY u.age ASC")
    List<User> findUsersOlderThan(@Param("minAge") int minAge);

    // Native SQL
    @Query(value = "SELECT * FROM users WHERE username LIKE CONCAT('%', :keyword, '%')",
           nativeQuery = true)
    List<User> searchUsernameNative(@Param("keyword") String keyword);
}
