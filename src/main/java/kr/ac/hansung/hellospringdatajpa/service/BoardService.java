package kr.ac.hansung.hellospringdatajpa.service;

import jakarta.persistence.EntityNotFoundException;
import kr.ac.hansung.hellospringdatajpa.dto.PageResponseDto;
import kr.ac.hansung.hellospringdatajpa.entity.Board;
import kr.ac.hansung.hellospringdatajpa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    // ── 기본 CRUD ──────────────────────────────────────────────────────────────

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<Board> findAll() {
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "seq"));
    }

    @Transactional(readOnly = true)
    public Board findById(Long seq) {
        return boardRepository.findById(seq)
                .orElseThrow(() -> new EntityNotFoundException("Board not found. seq: " + seq));
    }

    public Board update(Long seq, Board boardDetails) {
        Board board = findById(seq);
        board.setTitle(boardDetails.getTitle());
        board.setContent(boardDetails.getContent());
        board.setWriter(boardDetails.getWriter());
        return boardRepository.save(board);
    }

    public void deleteById(Long seq) {
        boardRepository.deleteById(seq);
    }

    // ── 페이징 & 정렬 ──────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public PageResponseDto<Board> findWithPageInfo(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "seq");
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return PageResponseDto.of(boardPage);
    }

    // ── [Query Method] ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Board> findByTitleContaining(String keyword) {
        return boardRepository.findByTitleContaining(keyword);
    }

    @Transactional(readOnly = true)
    public PageResponseDto<Board> findPageByTitle(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, "seq");
        Page<Board> boardPage = boardRepository.findPageByTitleContaining(keyword, pageable);
        return PageResponseDto.of(boardPage);
    }

    @Transactional(readOnly = true)
    public List<Board> findByWriter(String writer) {
        return boardRepository.findByWriter(writer);
    }

    // ── [@Query] ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Board> searchByTitlePositional(String keyword) {
        return boardRepository.searchByTitlePositional(keyword);
    }

    @Transactional(readOnly = true)
    public List<Board> searchByTitleNamed(String keyword) {
        return boardRepository.searchByTitleNamed(keyword);
    }

    @Transactional(readOnly = true)
    public List<Board> searchByTitleAndWriter(String keyword, String writer) {
        return boardRepository.searchByTitleAndWriter(keyword, writer);
    }

    @Transactional(readOnly = true)
    public List<Object[]> searchNativeByTitle(String keyword) {
        return boardRepository.searchNativeByTitle(keyword);
    }
}
