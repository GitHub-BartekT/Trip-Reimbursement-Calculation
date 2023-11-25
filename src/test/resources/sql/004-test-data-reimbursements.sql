insert into USER_GROUPS (name, daily_allowance,
                         cost_per_km, max_mileage, max_refund) values
                                  ('group_001_NoDailyAllowance', 0, 0.1, 1000, 100),
                                  ('group_002_NoCostPerKm', 10, 0, 100, 10),
                                  ('group_003_NoMaxMileage', 10, 0.25, 0, 500),
                                  ('group_004_NoMaxRefund', 10, 0.25, 100, 0),
                                  ('group_005_Ok', 10, 0.25, 500, 100),
                                  ('group_006_Ok', 10, 0.25, 500, 200);
insert into USERS (name, user_group_id) values
                                            ('user_01_noDailyRefund', 1),
                                            ('user_02_noCostPerKm', 2),
                                            ('user_03_noMaxMileage', 3),
                                            ('user_04_noRefund', 4),
                                            ('user_05_ok', 5),
                                            ('user_06_ok', 6),
                                            ('user_07_no_reimbursements', 6);

insert into REIMBURSEMENTS (name, start_date, end_date,
                   distance, pushed_to_accept, user_id) values
    ('reimbursement_001_zeroDaysNoRefund', null, '2022-03-20', 0, false, 1),
    ('reimbursement_002_oneDayNoRefund', '2022-03-21', '2022-03-21', 0, false, 1),
    ('reimbursement_003_moreDaysNoRefund', '2022-03-21', '2022-03-22', 0, false, 1),
    ('reimbursement_004_zeroDaysNoRefund', null, '2022-03-20', 0, false, 5),
    ('reimbursement_005_oneDayRefund', '2022-03-21', '2022-03-21', 0, false, 5),
    ('reimbursement_006_moreDaysRefund', '2022-03-21', '2022-03-25', 0, false, 5),
    ('reimbursement_007_moreDaysRefundMaxRefund', '2022-03-11', '2022-03-25', 0, false, 5),
    ('reimbursement_008_noCostPerKm', null, '2022-03-30', 100, false, 2),
    ('reimbursement_009_noMaxMileage', null, '2022-03-30', 100, false, 3),
    ('reimbursement_010_ok', null, '2022-03-30', 100, false, 5),
    ('reimbursement_011_noMaxMileage', null, '2022-03-30', 500, false, 5),
    ('reimbursement_012_noMaxRefund', '2022-03-21', '2022-03-30', 100, false, 4),
    ('reimbursement_013_ok', '2022-03-21', '2022-03-30', 100, false, 6);

