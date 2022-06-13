# Board SELECT
SELECT * FROM board

# Root Board Insert

# 베스트, 자유게시판, 커뮤니티, 건의사항, 기타
INSERT INTO board(BoardName, BoardOwnerID) VALUES('베스트', 'admin');
INSERT INTO board(BoardName, BoardOwnerID) VALUES('자유게시판', 'admin');
INSERT INTO board(BoardName, BoardOwnerID) VALUES('커뮤니티', 'admin');
INSERT INTO board(BoardName, BoardOwnerID) VALUES('건의사항', 'admin');
INSERT INTO board(BoardName, BoardOwnerID) VALUES('기타', 'admin');

# Child Board Insert

# 베스트 게시판 하위 게시판 INSERT
# 일일베스트 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID)
SELECT '일일베스트', 'admin', ID
FROM board
WHERE BoardName = '베스트';

# 주간베스트 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID)
SELECT '주간베스트', 'admin', ID
FROM board
WHERE BoardName = '베스트';

# 명예의전당 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID)
SELECT '명예의전당', 'admin', ID
FROM board
WHERE BoardName = '베스트';

# 커뮤니티 게시판 하위 게시판 INSERT
# 게임 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID)
SELECT '게임', 'admin', ID
FROM board
WHERE BoardName = '커뮤니티';

# 프로그래밍 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID)
SELECT '프로그래밍', 'admin', ID
FROM board
WHERE BoardName = '커뮤니티';

# 기타 게시판 하위 게시판 INSERT
# 공지사항 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID)
SELECT '공지사항', 'admin', ID
FROM board
WHERE BoardName = '기타';