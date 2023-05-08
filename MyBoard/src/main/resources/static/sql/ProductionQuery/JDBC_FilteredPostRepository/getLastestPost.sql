select *
from post
where board_id = :boardID
order by modify_day desc
limit :limit;