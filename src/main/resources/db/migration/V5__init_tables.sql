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
