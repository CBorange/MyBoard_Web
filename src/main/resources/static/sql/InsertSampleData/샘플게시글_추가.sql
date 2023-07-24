# 자유게시판 샘플 게시글 INSERT
DELIMITER $$
DROP PROCEDURE IF EXISTS loopSamplePostInsert;
CREATE PROCEDURE loopSamplePostInsert()
BEGIN
    DECLARE i INT;
    SET i = 1;
    WHILE (i <= 10) DO
            INSERT INTO post(BoardID, WriterID, Title, Content)
            SELECT ID, "admin", CONCAT("자유게시판 샘플 게시글 ", i), "자유게시판 샘플 내용"
            FROM board
            WHERE BoardName = '자유게시판';
            SET i = i + 1;
	END WHILE;
END $$
CALL loopSamplePostInsert();