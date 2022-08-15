# 게시글 추가
INSERT INTO post(BoardID, WriterID, Title, Content)
VALUES(:boardID, :writerID, :title, :content);