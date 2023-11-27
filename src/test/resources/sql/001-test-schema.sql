drop table if exists USER_COSTS;
drop table if exists USER_GROUPS_RECEIPT_TYPES;
drop table if exists RECEIPT_TYPES;
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
CREATE TABLE RECEIPT_TYPES (id INT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(100) NOT NULL,
                       max_value DECIMAL(10,2) NOT NULL DEFAULT 0
);
create table USER_GROUPS_RECEIPT_TYPES (
                       user_group_id int not null,
                       receipt_type_id int not null,
                       primary key (user_group_id, receipt_type_id),
                       foreign key (user_group_id) references USER_GROUPS (id),
                       foreign key (receipt_type_id) references RECEIPT_TYPES(id)
);
CREATE TABLE USER_COSTS (id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        cost_value DECIMAL(10,2) NOT NULL DEFAULT 0,
                        receipt_type_id int not null,
                        foreign key (receipt_type_id) references RECEIPT_TYPES (id),
                        reimbursement_id int not null,
                        foreign key (reimbursement_id) references REIMBURSEMENTS (id)
);
