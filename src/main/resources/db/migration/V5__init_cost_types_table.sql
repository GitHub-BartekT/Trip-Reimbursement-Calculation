create table COST_TYPES(
                              id int primary key auto_increment,
                              name varchar(100) not null,
                              max_value double default (null)
);

create table reimbursements_cost_types (
    reimbursement_id int not null,
    cost_type_id int not null,
    primary key (reimbursement_id, cost_type_id),
    foreign key (reimbursement_id) references REIMBURSEMENTS(id),
    foreign key (cost_type_id) references COST_TYPES (id)
);