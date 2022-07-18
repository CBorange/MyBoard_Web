# 자유게시판 샘플 게시글 INSERT
DELIMITER $$
DROP PROCEDURE IF EXISTS loopSampleInsert;
CREATE PROCEDURE loopSampleInsert()
BEGIN
    DECLARE i INT;
    SET i = 1;
    WHILE (i <= 85) DO
            INSERT INTO post(BoardID, WriterID, Title, Content)
            SELECT ID, "admin", CONCAT("자유게시판 샘플 게시글 ", i), "자유게시판 샘플 내용"
            FROM board
            WHERE BoardName = '자유게시판';
            SET i = i + 1;
	END WHILE;
END $$
CALL loopSampleInsert();