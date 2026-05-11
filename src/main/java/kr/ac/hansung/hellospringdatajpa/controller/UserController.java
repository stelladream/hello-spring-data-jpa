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

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
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

    // ── [Query Method] ────────────────────────────────────────────────────────

    /**
     *  - username  → findByUsername
     *  - keyword   → findByUsernameContaining (LIKE)
     *  - olderThan → findUsersOlderThan (@Query)
     *  - (없음)    → findAll
     */
    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer olderThan) {

        if (username != null) {
            return ResponseEntity.ok(userService.findByUsername(username));
        } else if (keyword != null) {
            return ResponseEntity.ok(userService.findByUsernameContaining(keyword));
        } else if (olderThan != null) {
            return ResponseEntity.ok(userService.findUsersOlderThan(olderThan));
        } else {
            return ResponseEntity.ok(userService.findAll());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── [@Query] ──────────────────────────────────────────────────────────────

    @GetMapping("/search/native")
    public ResponseEntity<List<User>> searchNative(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsernameNative(keyword));
    }
}
