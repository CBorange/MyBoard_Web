# MyBoard_Web
Spring 커뮤니티 게시판 프로젝트
스프링부트 및 자바, Bootstrap 등을 사용하여 게시판 만들기 토이프로젝트 입니다.

사용기술은 다음과 같습니다.
SpringBoot + MySQL(Backend)
Bootstrap + HTML + JS + Thymeleaf(Frontend)
AWS + Docker + Jenkins(Devops)

다음은 진행내역 입니다. 저장소 소유자 개인 진행상태 확인용입니다. 수시로 변경될 수 있습니다.

*일감(버그)*
1. 게시글 작성(Insert) 시 errocode 500 반환됨 확인 필요
2. 게시글 작성 중 이미지 수평정렬 값 변경 시 저장데이터(HTML content)에 반영안됨, 항상 좌측 정렬로 표시됨

*일감(구현)*
1. 게시글 수정 기능구현
2. 메인화면(home HTML) 전체 게시판 표시 및 베스트 게시물 표시
3. 기본 로그인(유저관리, SpringSecurity, JWT로 구현) 기능 구현
4. Google, Naver, Kakao 등 연동 로그인 기능 구현(OAuth)

*일감(구현됨)*
--1. 게시글 작성 기능-- (버그 존재함, 작업중)
2. 댓글 작성 기능
2-1. 대댓글 작성 기능
