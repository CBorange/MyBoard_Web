#게시글 수정
UPDATE post SET Title = :title, Content = :content, WriterID = :writerID
WHERE ID = :postID