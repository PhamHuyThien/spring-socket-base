select up.id as id,
       username as username,
       password as password,
       gold as gold,
       sliver as sliver,
       status as status,
       create_at as createAt,
       update_at as updateAt,
       delete_at as deleteAt
from user_profile up
         join user_wallet uw on uw.user_id = up.id
where up.username = ?1