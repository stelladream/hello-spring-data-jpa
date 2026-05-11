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

    // ── [Query Method] ────────────────────────────────────────────────────────

    /**
     *  - keyword + page → findPageByTitle (Page<T> 메타정보 포함)
     *  - keyword        → findByTitleContaining
     *  - writer         → findByWriter
     *  - page           → findWithPageInfo (전체 페이징)
     *  - (없음)         → findAll
     */
    @GetMapping
    public ResponseEntity<?> getBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String writer,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        int pageNum = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 10;

        if (keyword != null && page != null) {
            return ResponseEntity.ok(boardService.findPageByTitle(keyword, pageNum, pageSize));
        } else if (keyword != null) {
            return ResponseEntity.ok(boardService.findByTitleContaining(keyword));
        } else if (writer != null) {
            return ResponseEntity.ok(boardService.findByWriter(writer));
        } else if (page != null) {
            return ResponseEntity.ok(boardService.findWithPageInfo(pageNum, pageSize));
        } else {
            return ResponseEntity.ok(boardService.findAll());
        }
    }

    // ── [@Query] ──────────────────────────────────────────────────────────────

    /**
     *  - keyword + writer → searchByTitleAndWriter (복합 조건)
     *  - type=positional  → searchByTitlePositional (?1)
     *  - type=native      → searchNativeByTitle (Native SQL)
     *  - (기본값)         → searchByTitleNamed (@Param)
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
}
