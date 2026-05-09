package kr.ac.hansung.hellospringdatajpa.common;

import kr.ac.hansung.hellospringdatajpa.entity.Board;
import kr.ac.hansung.hellospringdatajpa.entity.User;
import kr.ac.hansung.hellospringdatajpa.repository.BoardRepository;
import kr.ac.hansung.hellospringdatajpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public void run(String... args) {

        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                new User(null, "홍길동", 28, "hong@hansung.ac.kr"),
                new User(null, "김철수", 35, "kim@hansung.ac.kr"),
                new User(null, "이영희", 22, "lee@hansung.ac.kr"),
                new User(null, "박민준", 41, "park@hansung.ac.kr"),
                new User(null, "최수연", 30, "choi@hansung.ac.kr"),
                new User(null, "정다은", 27, "jung@hansung.ac.kr"),
                new User(null, "강지훈", 33, "kang@hansung.ac.kr"),
                new User(null, "윤서진", 25, "yoon@hansung.ac.kr"),
                new User(null, "임재원", 38, "lim@hansung.ac.kr"),
                new User(null, "홍길동", 45, "hong2@hansung.ac.kr")
            ));
        }

        if (boardRepository.count() == 0) {
            List<Board> boards = new ArrayList<>();
            String[] writers = {"홍길동", "김철수", "이영희", "박민준", "최수연"};
            for (int i = 1; i <= 20; i++) {
                String title = (i % 2 == 0)
                    ? i + "번째 한성대 공지사항"
                    : i + "번째 일반 게시글";
                String content = title + " 본문 내용입니다.";
                boards.add(new Board(null, title, content, writers[i % 5], null));
            }
            boardRepository.saveAll(boards);
        }
    }
}
