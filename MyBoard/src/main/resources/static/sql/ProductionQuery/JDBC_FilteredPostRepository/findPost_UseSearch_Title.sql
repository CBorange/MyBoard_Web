# 제목으로 게시글 조회
SELECT @ROWNUM:=@ROWNUM+1 as OrderedPostNo, p.*
FROM post p,
    (SELECT @ROWNUM:=0) R
WHERE BoardID = :boardID AND Title LIKE :condition      # 변수 게시판 ID, 검색 조건(제목)
ORDER BY :sortColumn;   # 변수 정렬순서(ASC or DESC)