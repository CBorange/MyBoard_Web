# Root 게시판
# 베스트, 자유게시판, 커뮤니티, 건의사항, 기타
INSERT INTO board(BoardName, BoardOwnerID, SortOrder)
VALUES('베스트', 'admin', 0);

INSERT INTO board(BoardName, BoardOwnerID, SortOrder)
VALUES('자유게시판', 'admin', 0);

INSERT INTO board(BoardName, BoardOwnerID, SortOrder)
VALUES('커뮤니티', 'admin', 0);

INSERT INTO board(BoardName, BoardOwnerID, SortOrder)
VALUES('건의사항', 'admin', 0);

INSERT INTO board(BoardName, BoardOwnerID, SortOrder)
VALUES('기타', 'admin', 0);

# 하위 게시판 INSERT

# 베스트 게시판 하위 게시판 INSERT
# 일일베스트 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder)
SELECT '일일베스트', 'admin', ID, 0
FROM board
WHERE BoardName = '베스트';

# 주간베스트 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder)
SELECT '주간베스트', 'admin', ID, 1
FROM board
WHERE BoardName = '베스트';

# 명예의전당 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder)
SELECT '명예의전당', 'admin', ID, 2
FROM board
WHERE BoardName = '베스트';

# 커뮤니티 게시판 하위 게시판 INSERT
# 게임 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder)
SELECT '게임', 'admin', ID, 0
FROM board
WHERE BoardName = '커뮤니티';

# 프로그래밍 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder)
SELECT '프로그래밍', 'admin', ID, 1
FROM board
WHERE BoardName = '커뮤니티';

# 기타 게시판 하위 게시판 INSERT
# 공지사항 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder)
SELECT '공지사항', 'admin', ID, 0
FROM board
WHERE BoardName = '기타';