drop table if exists USERS;
drop table if exists USER_GROUPS;

create table USER_GROUPS (
                             id int primary key auto_increment,
                             name varchar(100) not null,
                             daily_allowance DOUBLE DEFAULT (0.0),
                             cost_per_km     DOUBLE DEFAULT (0.0),
                             max_mileage     DOUBLE DEFAULT (0.0),
                             max_refund      DOUBLE DEFAULT (0.0)

);

create table USERS (
                       id int primary key auto_increment,
                       name varchar(100) not null ,
                       user_group_id int null,
                       foreign key (user_group_id) references USER_GROUPS (id)
);