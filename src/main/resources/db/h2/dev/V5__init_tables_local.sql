CREATE TABLE RECEIPT_TYPES (
                        id int primary key auto_increment,
                        name VARCHAR(100) NOT NULL,
                        max_value DOUBLE DEFAULT 0.00 NOT NULL
);
create table USER_GROUPS_RECEIPT_TYPES (
                        user_group_id int not null,
                        receipt_type_id int not null,
                        primary key (user_group_id, receipt_type_id),
                        foreign key (user_group_id) references USER_GROUPS (id),
                        foreign key (receipt_type_id) references RECEIPT_TYPES(id)
);
CREATE TABLE USER_COSTS (id int primary key auto_increment,
                        name VARCHAR(100) NOT NULL,
                        cost_value DOUBLE DEFAULT 0.00 NOT NULL ,
                        receipt_type_id int not null,
                        foreign key (receipt_type_id) references RECEIPT_TYPES (id),
                        reimbursement_id int not null,
                        foreign key (reimbursement_id) references REIMBURSEMENTS (id)
);
