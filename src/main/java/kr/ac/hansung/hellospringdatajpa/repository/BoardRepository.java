package kr.ac.hansung.hellospringdatajpa.repository;

import kr.ac.hansung.hellospringdatajpa.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // ── 기본 CRUD (JpaRepository 상속) ────────────────────────────────────────
    // save / findAll / findById / count / deleteById

    // ── [Query Method] ────────────────────────────────────────────────────────

    List<Board> findByTitleContaining(String keyword);

    Page<Board> findPageByTitleContaining(String keyword, Pageable pageable);

    List<Board> findByWriter(String writer);

    // ── [@Query] ──────────────────────────────────────────────────────────────

    // JPQL — 위치 기반 파라미터(?1)
    @Query("SELECT b FROM Board b WHERE b.title LIKE %?1% ORDER BY b.seq DESC")
    List<Board> searchByTitlePositional(String keyword);

    // JPQL — 이름 기반 파라미터(@Param)
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% ORDER BY b.seq DESC")
    List<Board> searchByTitleNamed(@Param("keyword") String keyword);

    // JPQL — 복합 조건 (제목 + 작성자)
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:title% AND b.writer = :writer ORDER BY b.seq DESC")
    List<Board> searchByTitleAndWriter(@Param("title") String title, @Param("writer") String writer);

    // Native SQL
    @Query(value = "SELECT seq, title, writer FROM board WHERE title LIKE CONCAT('%', :keyword, '%') ORDER BY seq DESC",
           nativeQuery = true)
    List<Object[]> searchNativeByTitle(@Param("keyword") String keyword);
}
