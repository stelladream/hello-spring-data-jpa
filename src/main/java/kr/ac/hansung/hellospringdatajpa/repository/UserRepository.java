package kr.ac.hansung.hellospringdatajpa.repository;

import kr.ac.hansung.hellospringdatajpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ── [Query Method] ────────────────────────────────────────────────────────

    // [Query Method] 이름으로 조회
    List<User> findByUsername(String username);

    // [Query Method] 이름 + 나이 초과 (AND + GreaterThan)
    List<User> findByUsernameAndAgeGreaterThan(String username, int age);

    // [Query Method] 이름 키워드 포함 (LIKE '%keyword%')
    List<User> findByUsernameContaining(String keyword);

    // [Query Method] 나이 범위 (BETWEEN)
    List<User> findByAgeBetween(int min, int max);

    // [Query Method] 이메일로 단건 조회 (Optional 반환)
    Optional<User> findByEmail(String email);

    // [Query Method] 이름 같은 회원 나이 내림차순 정렬
    List<User> findByUsernameOrderByAgeDesc(String username);

    // ── [@Query] ──────────────────────────────────────────────────────────────

    // [@Query] 위치 기반 파라미터(?1) — JPQL
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.username = ?2")
    Optional<User> findByEmailAndUsernameQuery(String email, String username);

    // [@Query] 이름 기반 파라미터(@Param) — JPQL (권장 방식)
    @Query("SELECT u FROM User u WHERE u.age >= :minAge ORDER BY u.age ASC")
    List<User> findUsersOlderThan(@Param("minAge") int minAge);

    // [@Query] Native SQL — LIKE 키워드 검색
    @Query(value = "SELECT * FROM users WHERE username LIKE CONCAT('%', :keyword, '%')",
           nativeQuery = true)
    List<User> searchByUsernameNative(@Param("keyword") String keyword);
}
