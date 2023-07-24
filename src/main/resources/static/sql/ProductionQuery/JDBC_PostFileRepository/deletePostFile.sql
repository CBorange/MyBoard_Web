# 게시글 파일 삭제
DELETE FROM postfile
WHERE post_id = :postID AND file_id = :fileID