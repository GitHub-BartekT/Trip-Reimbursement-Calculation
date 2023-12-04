insert into USER_GROUPS (name) values
                                  ('CEO'),
                                  ('Sellers'),
                                  ('Regular employee'),
                                  ('Office employee'),
                                  ('Employee without receipts')
;
insert into RECEIPT_TYPES (name, max_value) values
                                  ('Train_AllUsers', 100),
                                  ('Aeroplane_CEO', 2000),
                                  ('Food_AllUsers', 45),
                                  ('Food_CEO', 450),
                                  ('Hotels_Sellers', 450),
                                  ('Other_Directors', 450)
;
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (1,1);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (2,1);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (3,1);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (4,1);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (1,2);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (1,3);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (2,3);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (3,3);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (4,3);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (1,4);
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values (2,5);