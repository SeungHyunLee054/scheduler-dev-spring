# Scheduler_dev
일정 관리 프로그램

## 기능
### 1. 회원 가입
- 유저명, 이메일, 비밀번호를 입력받아 회원가입
  - 이메일이 중복되면 회원가입 실패
  - 유저명은 4글자 내, 비밀번호는 8글자 이상, 이메일은 이메일 형식
### 2. 로그인
- 이메일, 비밀번호를 입력받아 유저의 id와 email이 담긴 session을 생성
### 3. 유저 조회
- 유저 전체 조회, pageIdx(default 0)와 pageSize(default 10)를 입력받아 한 페이지 당 보여줄 유저를 보여줌
- 수정된 날짜 순으로 정렬
### 4. 유저 수정
- 유저 본인의 유저명, 비밀번호를 수정
- 유저명은 4글자 내, 비밀번호는 8글자 이상
### 5. 유저 삭제
- 유저 본인의 회원 정보를 삭제
### 6. 일정 생성
- session에 담긴 유저정보를 가져와 할일 제목, 할일 내용을 입력받아 일정 생성
- 제목은 10글자 내, 내용은 200글자 내
### 7. 일정 조회 
- 일정 전체 조회, pageIdx(default 0)와 pageSize(default 10)를 입력받아 한 페이지 당 보여줄 일정을 보여줌
- 수정된 날짜 순으로 정렬
### 8. 일정 수정
- session에 담긴 유저 정보와 일정을 작성한 유저 정보가 일치하면 일정 제목, 일정 내용을 입력받아 일정 수정
- - 제목은 10글자 내, 내용은 200글자 내
### 9. 일정 삭제
- session에 담긴 유저 정보와 일정을 작성한 유저 정보가 일치하면 일정 삭제
### 10. 댓글 생성
- 일정의 id와 댓글 내용을 입력받아 해당 일정에 댓글 작성
- 내용은 50글자 내
### 11. 댓글 조회
- 일정의 id를 입력받아 해당 일정의 댓글 전체 조회, pageIdx(default 0)와 pageSize(default 10)를 입력받아 한 페이지 당 보여줄 댓글을 보여줌
- 수정된 날짜 순으로 정렬
### 12. 댓글 수정
- session에 담긴 유저 정보와 댓글을 작성한 유저 정보가 일치하면 댓글 내용을 입력받아 댓글 수정
- 내용은 50글자 내
### 13. 댓글 삭제
- session에 담긴 유저 정보와 댓글을 작성한 유저 정보가 일치하면 댓글 삭제

## [ERD](https://seunghyun937.notion.site/ERD-1c3c72e4644580f09a32c13c7413cf28?pvs=4)
## [API 명세서](https://seunghyun937.notion.site/API-1c3c72e464458083aa94c7e7e3c36504?pvs=4)
## [트러블슈팅](https://seunghyun937.notion.site/1c3c72e464458026894dd4d89852f4f2?pvs=4)

## 기술 스택
### Language
- Java17
### Framework
- SpringBoot 3.4.3
### Build Tool
- Gradle
### Database
- MySQL

## 프로젝트 폴더 구조
```` text
├───common
│   ├───constants
│   ├───exception
│   │   ├───dto
│   │   └───handler
│   ├───filter
│   │   ├───config
│   │   └───exception
│   ├───jpa
│   │   ├───audit
│   │   └───config
│   ├───log
│   │   ├───error
│   │   └───info
│   ├───response
│   ├───swagger
│   │   └───config
│   └───utils
│       └───password
│           └───exception
└───module
    ├───comment
    │   ├───application
    │   ├───controller
    │   ├───domain
    │   │   └───model
    │   ├───dto
    │   │   ├───request
    │   │   └───response
    │   ├───exception
    │   ├───repository
    │   └───service
    ├───member
    │   ├───controller
    │   ├───domain
    │   │   └───model
    │   ├───dto
    │   │   ├───request
    │   │   └───response
    │   ├───exception
    │   ├───repository
    │   └───service
    └───scheduler
        ├───application
        ├───controller
        ├───domain
        │   └───model
        ├───dto
        │   ├───request
        │   └───response
        ├───exception
        ├───repository
        └───service
````