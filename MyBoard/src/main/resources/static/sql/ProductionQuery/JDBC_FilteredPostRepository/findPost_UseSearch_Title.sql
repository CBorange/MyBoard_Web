# 제목으로 게시글 조회
# MySql DB에서는 OracleDB와 다르게 ROWNUM 메소드 지원 안함 사용자 정의 변수로 직접 만듦
SELECT @ROWNUM:=@ROWNUM+1 as OrderedPostNo, p.*     # ROWNUM 변수 Row 마다 +1 씩 추가
FROM post p,
    (SELECT @ROWNUM:=0) R   # FROM 절에서 ROWNUM 사용자 변수 선언, 0으로 초기화
WHERE BoardID = :boardID AND Title LIKE :condition      # 변수 게시판 ID, 검색 조건(제목)
ORDER BY :sortColumn;   # 변수 정렬순서(ASC or DESC)