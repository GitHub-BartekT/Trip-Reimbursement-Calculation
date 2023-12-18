alter table USER_GROUPS add column daily_allowance DOUBLE DEFAULT (0.0);
alter table USER_GROUPS add column cost_per_km DOUBLE DEFAULT (0.0);
alter table USER_GROUPS add column max_mileage DOUBLE DEFAULT (0.0);
alter table USER_GROUPS add column max_refund DOUBLE DEFAULT (0.0);