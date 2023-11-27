drop table if exists REIMBURSEMENTS_USER_COSTS;
drop table if exists USER_COSTS;
drop table if exists REIMBURSEMENTS;
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

create table REIMBURSEMENTS(
                               id int primary key auto_increment,
                               name varchar(100) not null,
                               start_date date default (null),
                               end_date date not null,
                               distance int default (0),
                               pushed_to_accept bit default false,
                               user_id int not null ,
                               foreign key (user_id) references USERS (id)
);
CREATE TABLE USER_COSTS (id INT PRIMARY KEY AUTO_INCREMENT,
                         name VARCHAR(100) NOT NULL,
                         cost_value DECIMAL(10,2) NOT NULL DEFAULT 0);

create table REIMBURSEMENTS_USER_COSTS (
                                           reimbursement_id int not null,
                                           user_cost_id int not null,
                                           primary key (reimbursement_id, user_cost_id),
                                           foreign key (reimbursement_id) references REIMBURSEMENTS(id),
                                           foreign key (user_cost_id) references USER_COSTS (id)
);