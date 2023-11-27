CREATE TABLE USER_COSTS (id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        cost_value DECIMAL(10,2) NOT NULL DEFAULT 0);
create table reimbursements_user_costs (
                                           reimbursement_id int not null,
                                           user_cost_id int not null,
                                           primary key (reimbursement_id, user_cost_id),
                                           foreign key (reimbursement_id) references REIMBURSEMENTS(id),
                                           foreign key (user_cost_id) references USER_COSTS (id)
);