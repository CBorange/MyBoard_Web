# Board SELECT
SELECT * FROM board

# Root Board Insert

# 베스트, 자유게시판, 커뮤니티, 건의사항, 기타
# 자유게시판, 건의사항 게시판은 하위 게시판이 없으므로 자체적으로 URL 연결함
INSERT INTO board(BoardName, BoardOwnerID, SortOrder) VALUES('베스트', 'admin', 0);
INSERT INTO board(BoardName, BoardOwnerID, SortOrder, ConnectURL) VALUES('자유게시판', 'admin', 1, '/board/general');
INSERT INTO board(BoardName, BoardOwnerID, SortOrder) VALUES('커뮤니티', 'admin', 2);
INSERT INTO board(BoardName, BoardOwnerID, SortOrder, ConnectURL) VALUES('건의사항', 'admin', 3, '/board/suggest');
INSERT INTO board(BoardName, BoardOwnerID, SortOrder) VALUES('기타', 'admin', 4);

# Child Board Insert

# 베스트 게시판 하위 게시판 INSERT
# 일일베스트 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder, ConnectURL)
SELECT '일일베스트', 'admin', ID, 0, '/board/dailybest'
FROM board
WHERE BoardName = '베스트';

# 주간베스트 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder, ConnectURL)
SELECT '주간베스트', 'admin', ID, 1, '/board/weeklybest'
FROM board
WHERE BoardName = '베스트';

# 명예의전당 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder, ConnectURL)
SELECT '명예의전당', 'admin', ID, 2, '/board/bestofbest'
FROM board
WHERE BoardName = '베스트';

# 커뮤니티 게시판 하위 게시판 INSERT
# 게임 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder, ConnectURL)
SELECT '게임', 'admin', ID, 0, '/board/game'
FROM board
WHERE BoardName = '커뮤니티';

# 프로그래밍 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder, ConnectURL)
SELECT '프로그래밍', 'admin', ID, 1, '/board/programing'
FROM board
WHERE BoardName = '커뮤니티';

# 기타 게시판 하위 게시판 INSERT
# 공지사항 게시판
INSERT INTO board(BoardName, BoardOwnerID, ParentBoardID, SortOrder, ConnectURL)
SELECT '공지사항', 'admin', ID, 0, '/board/notice'
FROM board
WHERE BoardName = '기타';