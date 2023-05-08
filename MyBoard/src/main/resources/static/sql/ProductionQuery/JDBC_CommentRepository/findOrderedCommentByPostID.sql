/# 게시글 댓글 조회
SELECT @ROWNUM:=@ROWNUM+1 as OrderedCommentNo, c.*      # ROWNUM 변수 Row 마다 +1 씩 추가
FROM comment c,
	 (SELECT @ROWNUM:=0) R   # FROM 절에서 ROWNUM 사용자 변수 선언, 0으로 초기화
WHERE post_id = :postID      # 변수 게시글 ID
ORDER BY modify_day DESC;     # 수정일자 기준으로 내림차순 정렬