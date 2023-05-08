# 게시글 추가
INSERT INTO post(board_id, writer_id, title, content)
VALUES(:boardID, :writerID, :title, :content);