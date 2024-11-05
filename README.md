# MyBoard
서비스 접속 : https://ltj-myboard.kro.kr/  
개발 블로그 : [링크](https://iceorange-etc.tistory.com/category/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D/SpringBoot)  

MyBoard는 Spring Boot로 개발한 커뮤니티 게시판 프로젝트 입니다.  
회원가입, 게시글작성, 댓글작성, 베스트게시글 조회, 유저정보 변경, 스크랩 등  
게시판 기반 커뮤니티 사이트들의 일반적인 기능들을 제공합니다.  
화면 및 서버개발, 배포까지 1인 개발로 진행하였습니다.

![로그인_유저정보](https://github.com/CBorange/MyBoard_Web/assets/31188689/209e9936-031e-46a9-b19e-d3d08751d48a)
![게시글_댓글_쓰기](https://github.com/CBorange/MyBoard_Web/assets/31188689/cca40345-b215-4b70-8300-f5e808e806d2)
![스크랩](https://github.com/CBorange/MyBoard_Web/assets/31188689/a2313ec7-34ab-447b-985c-ea93c91092e6)

# 구현기술
MyBoard 프로젝트는 다음과 같은 기술들로 개발되었습니다.  
Java/Spring Boot,   
Thymeleaf, BootStrap, Native JS, CSS  
MySQL, JPA, Redis  

# Client/Server 설계  

![image](https://github.com/user-attachments/assets/40b70a83-c091-4a55-a6ef-72e19f5d631f)

Client는 **Native JS**로 개발하였습니다.  
Server의 API Request Flow는 **Controller, Service, Repository** 3가지 모듈의 연결로 구현하였습니다.  
Service는 핵심 비즈니스 로직을 캡슐화 하고 트랜잭션을 처리합니다.  
Repository는 DAO로써 요구사항에 따라 DB에 CRUD를 실행합니다.  
**AOP** 개념을 적용하여 오류 발생시 예외처리 및 Logging 처리를 분리하여 개발하였습니다.  

**단위, 통합 테스트 코드**를 작성하여 기능을 테스트하며 작업했습니다.  
특히 유저 기능을 개발하는데 있어서 DTO Validation 기능을 테스트 할 때  
빠르게 작업할 수 있었습니다.  

# DB 설계  

![image](https://github.com/CBorange/MyBoard_Web/assets/31188689/f9408803-f968-4e85-aa7e-7b7f4ef8bdcc)  
ERD Cloud : [링크](https://www.erdcloud.com/d/t6oFY6dkMXK3EdH4F)  
MySQL을 사용하여 게시판 DB를 설계하였습니다.

# 배포환경  

![image](https://github.com/CBorange/MyBoard_Web/assets/31188689/6422930f-4528-43d2-a31f-3fb8f67b5584)

AWS EC2 환경(Linux Ubuntu Host) 에서 Docker Container 가상화를 통해 구성하였습니다.  

![image](https://github.com/user-attachments/assets/b41ccde0-12a3-4d7b-9c2f-04b3551bb944)

Jekins, Git을 통해 CI/CD 환경을 구축하였습니다.  
Dev, Release 서버를 각각 다른 Container로 구성하여 개발/배포 환경을 분리하였습니다.  
Nginx Reverse Proxy Server를 통해 하나의 Domain/SSL 인증서를 통해 서비스에 접속할 수 있도록 구성하였습니다.  
