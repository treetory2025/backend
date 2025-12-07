insert into member (member_id, uuid, email, nickname, role, is_deleted, created_at, modified_at) values
        (1, 'b8a3eb59-b956-4df9-8a55-80784016b8d4', 'test@test.com', '테스트유저', 'ROLE_USER', false, current_timestamp, current_timestamp),
        (2, '7dc08288-d5fb-490d-8ece-9e55e3b7dda4', 'test1@test.com', '2', 'ROLE_USER', false, current_timestamp, current_timestamp);

insert into bookmark (member_id, target_member_id) values
                                                       (1, 2);

insert into tree (member_id, size, theme, background) values
                                                          (1, 1, 'SNOWY', 'SILENT_NIGHT'),
                                                          (2, 1, 'SNOWY', 'SILENT_NIGHT');

insert into ornament (ornament_id, name, category, img_url, is_public, is_deleted, created_at, modified_at)
values (1, 'name', 'CHRISTMAS', 'urlurl', true, false, current_timestamp, current_timestamp);

insert into placed_ornament (placed_ornament_id, tree_id, ornament_id, position_x, position_y, message, writer_nickname,
                             font, is_deleted, created_at, modified_at)
values (1, 1, 1, 1, 1, 'message', 'nickni', 'NANUM_PEN', false, current_timestamp, current_timestamp);