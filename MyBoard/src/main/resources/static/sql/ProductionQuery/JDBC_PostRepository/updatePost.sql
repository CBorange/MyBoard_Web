#게시글 수정
UPDATE post SET title = :title, content = :content, writer_id = :writerID
WHERE id = :postID