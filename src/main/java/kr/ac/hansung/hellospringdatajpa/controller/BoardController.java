package kr.ac.hansung.hellospringdatajpa.controller;

import kr.ac.hansung.hellospringdatajpa.entity.Board;
import kr.ac.hansung.hellospringdatajpa.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // ── 기본 CRUD ──────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<Board> createBoard(@RequestBody Board board) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.save(board));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Board>> createBoards(@RequestBody List<Board> boards) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveAll(boards));
    }

    /**
     * 단일 GET 엔드포인트 — query param 조합으로 분기
     *  - keyword + page         → 제목 검색 + 페이징 (meta=true 시 Page<T> 메타정보 포함)
     *  - keyword + target=all   → 제목+내용 OR 검색
     *  - keyword                → 제목 키워드 검색
     *  - writer                 → 작성자 검색
     *  - seqFrom + seqTo        → seq 범위 검색
     *  - page (+ meta=true)     → 전체 페이징
     *  - (없음)                 → 전체 목록 (seq DESC)
     */
    @GetMapping
    public ResponseEntity<?> getBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String target,
            @RequestParam(required = false) String writer,
            @RequestParam(required = false) Long seqFrom,
            @RequestParam(required = false) Long seqTo,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) Boolean meta) {

        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;
        boolean withMeta = Boolean.TRUE.equals(meta);

        if (keyword != null && page != null) {
            if (withMeta) {
                return ResponseEntity.ok(boardService.findPageByTitle(keyword, pageNum, pageSize));
            } else {
                return ResponseEntity.ok(boardService.findByTitleWithPage(keyword, pageNum, pageSize));
            }
        } else if (keyword != null && "all".equals(target)) {
            return ResponseEntity.ok(boardService.findByTitleContainingOrContentContaining(keyword));
        } else if (keyword != null) {
            return ResponseEntity.ok(boardService.findByTitleContaining(keyword));
        } else if (writer != null) {
            return ResponseEntity.ok(boardService.findByWriter(writer));
        } else if (seqFrom != null && seqTo != null) {
            return ResponseEntity.ok(boardService.findBySeqBetween(seqFrom, seqTo));
        } else if (page != null) {
            if (withMeta) {
                return ResponseEntity.ok(boardService.findWithPageInfo(pageNum, pageSize));
            } else {
                return ResponseEntity.ok(boardService.findByPage(pageNum, pageSize));
            }
        } else {
            return ResponseEntity.ok(boardService.findAll());
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(boardService.count());
    }

    /**
     * @Query 학습용 엔드포인트 — type 파라미터로 구현 방식 선택
     *  - type=positional → @Query 위치 기반 파라미터 (?1)
     *  - type=named      → @Query 이름 기반 파라미터 (@Param)
     *  - type=native     → @Query nativeQuery=true (Native SQL)
     *  - writer 포함     → @Query 복합 조건 (제목 + 작성자)
     */
    @GetMapping("/query")
    public ResponseEntity<?> query(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String writer) {

        String kw = (keyword != null) ? keyword : "";

        if (keyword != null && writer != null) {
            return ResponseEntity.ok(boardService.searchByTitleAndWriter(kw, writer));
        } else if ("positional".equals(type)) {
            return ResponseEntity.ok(boardService.searchByTitlePositional(kw));
        } else if ("native".equals(type)) {
            return ResponseEntity.ok(boardService.searchNativeByTitle(kw));
        } else {
            return ResponseEntity.ok(boardService.searchByTitleNamed(kw));
        }
    }

    @GetMapping("/{seq}")
    public ResponseEntity<Board> getById(@PathVariable Long seq) {
        return ResponseEntity.ok(boardService.findById(seq));
    }

    @PutMapping("/{seq}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long seq, @RequestBody Board board) {
        return ResponseEntity.ok(boardService.update(seq, board));
    }

    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long seq) {
        boardService.deleteById(seq);
        return ResponseEntity.noContent().build();
    }
}
