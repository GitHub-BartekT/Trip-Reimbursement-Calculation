insert into USER_GROUPS (name) values ('Office_User');
insert into USER_GROUPS (name) values ('Regular_User');
insert into USERS (name, user_group_id) values ('Admin_01', 1);
insert into USERS (name, user_group_id) values ('Guest_User_01', 2);
COMMIT ;