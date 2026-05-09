# Hello Spring Data JPA

## 1. 프로젝트 개요

Spring Data JPA 강의의 핵심 개념을 실습하는 프로젝트입니다.  
`UserRepository`와 `BoardRepository`를 중심으로 기본 CRUD, 페이징/정렬, Query Method, @Query를 학습합니다.

## 2. 기술 스택

| 항목 | 버전 |
|------|------|
| Spring Boot | 4.0.6 |
| Spring Data JPA | (Boot 의존) |
| MySQL | 9.0 (Docker) |
| Docker Compose | - |
| Maven | 3.9+ |
| Java | 21 |
| Lombok | (Boot 의존) |

## 3. 실행 방법

```bash
# MySQL 9.0 컨테이너 실행
docker compose up -d

# 컨테이너 상태 확인
docker compose ps

# 애플리케이션 실행
mvn spring-boot:run
```

## 4. MySQL 접속 정보

| 항목 | 값 |
|------|----|
| Host | localhost |
| Port | 3306 |
| Database | springjpa |
| User | jpauser |
| Password | jpa1234 |

## 5. Postman Collection 사용법

1. Postman을 실행합니다.
2. **Import** 버튼 클릭
3. `postman/hello-spring-data-jpa.postman_collection.json` 파일 선택
4. Collection 변수 `baseUrl`이 `http://localhost:8080`으로 설정되어 있습니다.
5. 각 요청의 description에 어떤 Spring Data JPA 개념인지 설명이 있습니다.

## 6. 학습 개념 요약

### UserRepository

| 개념 | 메서드 예시 |
|------|------------|
| 기본 CRUD | `save()`, `findById()`, `findAll()`, `deleteById()`, `count()`, `existsById()` |
| Query Method | `findByUsername()`, `findByUsernameContaining()`, `findByAgeBetween()` |
| Query Method (AND) | `findByUsernameAndAgeGreaterThan()` |
| Query Method (Optional) | `findByEmail()` |
| @Query (Positional) | `findByEmailAndUsernameQuery()` — `?1`, `?2` 파라미터 |
| @Query (Named Param) | `findUsersOlderThan()` — `:minAge` 파라미터 (권장) |
| @Query (Native) | `searchByUsernameNative()` — `nativeQuery = true` |

### BoardRepository

| 개념 | 메서드 예시 |
|------|------------|
| 기본 CRUD | `save()`, `findAll(Sort)`, `findAll(Pageable)` |
| 페이징 (List) | `findByTitleContaining(kw, Pageable)` → List 반환 |
| 페이징 (Page\<T\>) | `findPageByTitleContaining(kw, Pageable)` → 메타정보 포함 |
| Query Method (Or) | `findByTitleContainingOrContentContaining()` |
| Query Method (Between) | `findBySeqBetween()` |
| @Query (Positional) | `searchByTitlePositional()` — `%?1%` |
| @Query (Named Param) | `searchByTitleNamed()` — `%:keyword%` (권장) |
| @Query (복합 조건) | `searchByTitleAndWriter()` |
| @Query (Native) | `searchNativeByTitle()` — `nativeQuery = true` |

## 7. 전체 API 엔드포인트 목록

### User API (`/api/users`)

| Method | URL | 설명 | 개념 |
|--------|-----|------|------|
| POST | `/api/users` | 회원 단건 등록 | `save()` |
| POST | `/api/users/batch` | 회원 다건 등록 | `saveAll()` |
| GET | `/api/users` | 전체 목록 | `findAll()` |
| GET | `/api/users/{id}` | 단건 조회 | `findById()` |
| PUT | `/api/users/{id}` | 수정 | `save()` |
| DELETE | `/api/users/{id}` | 삭제 | `deleteById()` |
| GET | `/api/users/count` | 전체 건수 | `count()` |
| GET | `/api/users/{id}/exists` | 존재 여부 | `existsById()` |
| GET | `/api/users?username=` | 이름 조회 | Query Method |
| GET | `/api/users?username=&minAge=` | 이름+나이 초과 | Query Method (AND+GreaterThan) |
| GET | `/api/users?keyword=` | 이름 키워드 | Query Method (Containing) |
| GET | `/api/users?minAge=&maxAge=` | 나이 범위 | Query Method (Between) |
| GET | `/api/users/email/{email}` | 이메일 조회 | Query Method (Optional) |
| GET | `/api/users?olderThan=` | 나이 이상 목록 | @Query (Named Param) |
| GET | `/api/users/verify?email=&username=` | 이메일+이름 인증 | @Query (Positional) |
| GET | `/api/users/search/native?keyword=` | Native SQL 검색 | @Query (nativeQuery) |

### Board API (`/api/boards`)

| Method | URL | 설명 | 개념 |
|--------|-----|------|------|
| POST | `/api/boards` | 게시글 단건 등록 | `save()` |
| POST | `/api/boards/batch` | 게시글 다건 등록 | `saveAll()` |
| GET | `/api/boards` | 전체 목록 (seq DESC) | `findAll(Sort)` |
| GET | `/api/boards/{seq}` | 단건 조회 | `findById()` |
| PUT | `/api/boards/{seq}` | 수정 | `save()` |
| DELETE | `/api/boards/{seq}` | 삭제 | `deleteById()` |
| GET | `/api/boards/count` | 전체 건수 | `count()` |
| GET | `/api/boards?page=&size=` | 페이징 | `findAll(Pageable)` |
| GET | `/api/boards?page=&size=&meta=true` | 페이징 + 메타정보 | `Page<T>` |
| GET | `/api/boards?keyword=` | 제목 키워드 검색 | Query Method (Containing) |
| GET | `/api/boards?keyword=&target=all` | 제목+내용 OR 검색 | Query Method (Or) |
| GET | `/api/boards?keyword=&page=&size=` | 검색 + 페이징 | Query Method + Pageable |
| GET | `/api/boards?keyword=&page=&size=&meta=true` | 검색 + Page\<T\> | Query Method + Page\<T\> |
| GET | `/api/boards?writer=` | 작성자 조회 | Query Method |
| GET | `/api/boards?seqFrom=&seqTo=` | seq 범위 조회 | Query Method (Between) |
| GET | `/api/boards/query?keyword=&type=positional` | @Query 위치 파라미터 | @Query (?1) |
| GET | `/api/boards/query?keyword=&type=named` | @Query 이름 파라미터 | @Query (@Param) |
| GET | `/api/boards/query?keyword=&type=native` | Native SQL | @Query (nativeQuery) |
| GET | `/api/boards/query?keyword=&writer=` | @Query 복합 조건 | @Query (복합) |
