# 댓글 추가
INSERT INTO comment(post_id, parent_comment_id, writer_id, content)
VALUES(:postID, :parentCommentID, :writerID, :content);