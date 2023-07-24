# 제목으로 게시글 조회
SELECT p.*, COALESCE(c.CommentCount, 0) as CommentCount
FROM # 서브쿼리로 게시글 조회
	(SELECT @ROWNUM:=@ROWNUM+1 as OrderedPostNo, p.*     # ROWNUM 변수 Row 마다 +1 씩 추가
	FROM post p,
	(SELECT @ROWNUM:=0) R # 서브쿼리로 ROWNUM 사용자 변수 선언, 0으로 초기화
	WHERE board_id = :boardID AND title LIKE :condition      # 변수 : 게시판 ID, 검색 조건(제목)
	ORDER BY modify_day DESC) p
	# 서브쿼리로 각 게시글별 댓글 개수 조회, 게시글 조회 결과와 조인
    LEFT JOIN (SELECT c.post_id, count(*) as CommentCount
			   FROM comment c
               GROUP BY post_id) c
    ON (p.id = c.post_id)