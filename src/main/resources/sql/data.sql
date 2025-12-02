insert into member (member_id, uuid, email, nickname, role, is_deleted, created_at, modified_at) values
        (1, 'b8a3eb59-b956-4df9-8a55-80784016b8d4', 'test@test.com', '테스트유저', 'ROLE_USER', false, current_timestamp, current_timestamp),
        (2, '7dc08288-d5fb-490d-8ece-9e55e3b7dda4', 'test1@test.com', '2', 'ROLE_USER', false, current_timestamp, current_timestamp);

insert into tree (member_id, size, theme, background) values
                                                          (1, 1, 'SNOWY', 'SILENT_NIGHT'),
                                                          (2, 1, 'SNOWY', 'SILENT_NIGHT');