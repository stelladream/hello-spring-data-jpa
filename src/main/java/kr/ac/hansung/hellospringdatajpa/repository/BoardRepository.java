package kr.ac.hansung.hellospringdatajpa.repository;

import kr.ac.hansung.hellospringdatajpa.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // ── [Query Method] ────────────────────────────────────────────────────────

    // [Query Method] 제목에 키워드 포함 (LIKE '%keyword%')
    List<Board> findByTitleContaining(String keyword);

    // [Query Method] 제목에 키워드 포함 + 페이징 (List 반환 — 메타정보 없음)
    List<Board> findByTitleContaining(String keyword, Pageable pageable);

    // [Query Method] 제목에 키워드 포함 + 페이징 (Page<T> 반환 — totalElements 등 메타정보 포함)
    Page<Board> findPageByTitleContaining(String keyword, Pageable pageable);

    // [Query Method] 제목 OR 내용에 키워드 포함
    List<Board> findByTitleContainingOrContentContaining(String title, String content);

    // [Query Method] 작성자로 조회
    List<Board> findByWriter(String writer);

    // [Query Method] seq 범위 조회 (BETWEEN)
    List<Board> findBySeqBetween(Long start, Long end);

    // ── [@Query] ──────────────────────────────────────────────────────────────

    // [@Query] 위치 기반 파라미터(?1) — JPQL
    @Query("SELECT b FROM Board b WHERE b.title LIKE %?1% ORDER BY b.seq DESC")
    List<Board> searchByTitlePositional(String keyword);

    // [@Query] 이름 기반 파라미터(@Param) — JPQL (권장 방식)
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% ORDER BY b.seq DESC")
    List<Board> searchByTitleNamed(@Param("keyword") String keyword);

    // [@Query] 복합 조건 — 제목 + 작성자
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:title% AND b.writer = :writer ORDER BY b.seq DESC")
    List<Board> searchByTitleAndWriter(@Param("title") String title, @Param("writer") String writer);

    // [@Query] Native SQL — 제목 LIKE 검색 (일부 컬럼만 반환)
    @Query(value = "SELECT seq, title, writer FROM board WHERE title LIKE CONCAT('%', :keyword, '%') ORDER BY seq DESC",
           nativeQuery = true)
    List<Object[]> searchNativeByTitle(@Param("keyword") String keyword);
}
