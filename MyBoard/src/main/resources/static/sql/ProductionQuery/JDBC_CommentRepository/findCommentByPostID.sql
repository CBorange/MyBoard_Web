# 게시글 댓글 조회
SELECT *
FROM comment
WHERE PostID = :postID;