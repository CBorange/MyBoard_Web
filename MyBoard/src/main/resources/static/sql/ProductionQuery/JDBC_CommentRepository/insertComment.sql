# 댓글 추가
INSERT INTO comment(PostID, ParentCommentID, WriterID, Content)
VALUES(:postID, :parentCommentID, :writerID, :content);