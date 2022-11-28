# 게시글 파일 삭제
DELETE FROM postfile
WHERE PostID = :postID AND FileID = :fileID