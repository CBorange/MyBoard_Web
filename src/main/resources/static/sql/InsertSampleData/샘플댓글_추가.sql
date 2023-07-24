# 샘플게시글_추가.sql 먼저 실행 후 아래 쿼리 실행
# 자유게시판 게시글(자유게시판 샘플 게시글 1) 샘플 댓글 INSERT
DELIMITER //

DROP PROCEDURE IF EXISTS loopSampleCommentInsert //
CREATE PROCEDURE loopSampleCommentInsert()
BEGIN
    DECLARE i INT;
    SET i = 1;
    WHILE (i <= 300) DO
        INSERT INTO comment(post_id, writer_id, content)
        SELECT p.id, 'admin', CONCAT('테스트 Root 댓글 입력 ', i)
        FROM post p
        WHERE p.title = 'sdfgsd';
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

CALL loopSampleCommentInsert();

# 아래 쿼리 실행 시 또 2014 Commands out of sync; 발생 하는 경우 있음
# 쿼리 INSERT ~ WHERE 절 까지 부분 실행해보니까 문제 없음
# 왜 그러는지 모르겠는데 comment 테이블에 ROW 한 개 추가했다가 삭제 하고 실행하니까 동작함
# 자유게시판 게시글 샘플 댓글의 대댓글 INSERT
DELIMITER $$
DROP PROCEDURE IF EXISTS loopSampleSubCommentInsert;
CREATE PROCEDURE loopSampleSubCommentInsert (IN sampleCommentNum INT, IN subCommentCount INT)
BEGIN
    DECLARE i INT;
    SET i = 1;
    WHILE (i <= subCommentCount) DO
        INSERT INTO comment (post_id, parent_comment_id, writer_id, content)
        SELECT post_id, id, "admin", CONCAT("대댓글 입력 ", i)
        FROM comment
        WHERE content = CONCAT("테스트 Root 댓글 입력 ", sampleCommentNum);
        SET i = i + 1;
    END WHILE;
END $$

# 아래 PROCEDURE 호출은 1개씩 실행 왜 그런지 모르겠는데 한번에 실행하면
# 2014 Commands out of sync; 오류 발생함 한번에 한줄 씩 쿼리 실행하면 문제 없음
CALL loopSampleSubCommentInsert(1, 5);
CALL loopSampleSubCommentInsert(2, 4);
CALL loopSampleSubCommentInsert(3, 1);