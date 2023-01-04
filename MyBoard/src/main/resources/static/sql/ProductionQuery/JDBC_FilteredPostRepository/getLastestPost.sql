select *
from post
where BoardID = :boardID
order by ModifyDay desc
limit :limit;