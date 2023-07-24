# 게시글 ID로 게시글 파일정보 획득
SELECT * FROM postfile
WHERE post_id = :postID;