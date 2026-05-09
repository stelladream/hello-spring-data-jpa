package kr.ac.hansung.hellospringdatajpa.controller;

import kr.ac.hansung.hellospringdatajpa.entity.User;
import kr.ac.hansung.hellospringdatajpa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ── 기본 CRUD ──────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<User>> createUsers(@RequestBody List<User> users) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveAll(users));
    }

    /**
     * 단일 GET 엔드포인트에서 query param 조합으로 분기
     *  - username + minAge → findByUsernameAndAgeGreaterThan (Query Method AND+GreaterThan)
     *  - username          → findByUsername (Query Method)
     *  - keyword           → findByUsernameContaining (Query Method Containing)
     *  - minAge + maxAge   → findByAgeBetween (Query Method Between)
     *  - olderThan         → findUsersOlderThan (@Query Named Param)
     *  - (없음)            → findAll
     */
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Integer olderThan) {

        if (username != null && minAge != null) {
            return ResponseEntity.ok(userService.findByUsernameAndAgeGreaterThan(username, minAge));
        } else if (username != null) {
            return ResponseEntity.ok(userService.findByUsername(username));
        } else if (keyword != null) {
            return ResponseEntity.ok(userService.findByUsernameContaining(keyword));
        } else if (minAge != null && maxAge != null) {
            return ResponseEntity.ok(userService.findByAgeBetween(minAge, maxAge));
        } else if (olderThan != null) {
            return ResponseEntity.ok(userService.findUsersOlderThan(olderThan));
        } else {
            return ResponseEntity.ok(userService.findAll());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(userService.count());
    }

    // [@Query] 위치 기반 파라미터 — 이메일+이름으로 회원 인증 조회
    @GetMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestParam String email,
            @RequestParam String username) {
        return userService.verifyByEmailAndUsername(email, username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // [@Query] Native SQL — 이름 키워드 검색
    @GetMapping("/search/native")
    public ResponseEntity<List<User>> searchNative(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchByUsernameNative(keyword));
    }

    // [Query Method] 이메일로 단건 조회
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        return ResponseEntity.ok(userService.existsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
