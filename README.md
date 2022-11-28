# MyBoard_Web  
Spring 커뮤니티 게시판 프로젝트  
스프링부트 및 자바, Bootstrap 등을 사용하여 게시판 만들기 토이프로젝트 입니다.  
  
## 사용기술은 다음과 같습니다.  
SpringBoot + MySQL(Backend)  
Bootstrap + Thymeleaf(Frontend)  
AWS + Docker + Jenkins(Devops)  

## 다음은 코딩 규칙입니다.
  
**Service 객체 구현 규칙**  
  
Service 객체는 Transaction을 관리하는 함수와 Repository를 호출하는 함수의 조합으로 구현된다.  
Transaction을 관리하는 함수는 접미어에 Process를 붙여서 명명해야 한다.  
Repository를 호출하는 함수는 일반 자바 함수 명명규칙을 따른다.  
ex) 게시글을 삽입하는 기능을 구현하는 경우 insertPostProcess(Transactional 관리, exception 발생 시 예외처리 구현)  
insertPost(Repository 호출, Transaction 결과 유효성 검사) 총 두가지 함수를 구현한다.  
  
## 다음은 진행내역 입니다. 작업 진행상태 확인용입니다  
  
**일감(버그)**  
1. 게시글 작성 중 이미지 수평정렬 값 변경 시 저장데이터(HTML content)에 반영안됨, 항상 좌측 정렬로 표시됨  
-> post_image CSS Class로 인해서 항성 좌측정렬됨, 현재는 의도된 결과물임 추후 개선방안 연구
  
**일감(구현)**  
- 스크랩 기능  
- 추천/비추천 기능
- 메인화면(home HTML) 전체 게시판 표시 및 베스트 게시물 표시  
- 기본 로그인(유저관리, SpringSecurity, JWT로 구현) 기능 구현  
- Google, Naver, Kakao 등 연동 로그인 기능 구현(OAuth)  
    
**일감(구현됨)**  
- 게시글 작성 기능  
- 게시글 수정 기능  
- 댓글 작성 기능  
- 대댓글 작성 기능  
