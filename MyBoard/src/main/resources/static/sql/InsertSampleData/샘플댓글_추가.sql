# 자유게시판 게시글 샘플 댓글 INSERT
DELIMITER $$
DROP PROCEDURE IF EXISTS loopSampleCommentInsert;
CREATE PROCEDURE loopSampleCommentInsert()
BEGIN
    DECLARE i INT;
    SET i = 1;
    WHILE (i <= 10) DO
            INSERT INTO comment(PostID, WriterID, Content)
            SELECT ID, "admin", CONCAT("테스트 Root 댓글 입력 ", i)
            FROM post
            WHERE Title = '자유게시판 샘플 게시글 1';
            SET i = i + 1;
	END WHILE;
END $$
CALL loopSampleCommentInsert();