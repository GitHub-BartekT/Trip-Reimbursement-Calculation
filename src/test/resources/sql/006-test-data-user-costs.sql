insert into USER_GROUPS (name, daily_allowance,
                         cost_per_km, max_mileage, max_refund) values
                                    ('CEO', 15, 0.3, 500, 2000),
                                    ('Sellers', 15, 0.25, 1000, 1000),
                                    ('Regular_employee', 15, 0.3, 150, 100),
                                    ('Office_employee', 15, 0, 0, 100)
;
insert into USERS (name, user_group_id) values
                                    ('Boss', 1),
                                    ('Seller_number_01', 2),
                                    ('Seller_number_02', 2),
                                    ('Regular_employee_1', 3),
                                    ('Regular_employee_2', 3),
                                    ('Office_employee_1', 4),
                                    ('Office_employee_2', 4),
                                    ('Office_employee_3', 4),
                                    ('Office_employee_4', 4)
;
insert into REIMBURSEMENTS (name, start_date, end_date,
                            distance, pushed_to_accept, user_id) values
                                    ('reimbursement_01_S1', null, '2022-03-30', 0, false, 2),
                                    ('reimbursement_02_S1', '2022-03-21', '2022-03-21', 0, false, 2),
                                    ('reimbursement_03_S2', null, '2022-03-20', 0, false, 3),
                                    ('reimbursement_03_S2', '2022-03-21', '2022-03-21', 0, false, 3),
                                    ('Regular_employee_2', '2022-03-21', '2022-03-25', 0, false, 5)
;

insert into RECEIPT_TYPES (name, max_value) values
                                    ('Train_AllUsers', 100),
                                    ('Aeroplane_CEO', 2000),
                                    ('Food_AllUsers', 45),
                                    ('Food_CEO', 450),
                                    ('Hotels_Sellers', 450),
                                    ('Other_Directors', 450)
;
insert into USER_GROUPS_RECEIPT_TYPES (user_group_id, receipt_type_id) values
                                    (1,1),(2,1),(3,1),(4,1),
                                    (1,2),
                                    (1,3),(2,3),(3,3),(4,3),
                                    (1,4),
                                          (2,5);
insert into USER_COSTS (name, cost_value, receipt_type_id, reimbursement_id) values
                                    ('Hotel',150,5,1),
                                    ('Branch',30,3,1),
                                    ('Train',25,1,1),
                                    ('Train',150,1,2),
                                    ('Motel',30,5,2),
                                    ('Hotel_02',25,5,3),
                                    ('Hotel',75,5,5),
                                    ('Branch',30,3,5),
                                    ('Train',25,1,5)
;