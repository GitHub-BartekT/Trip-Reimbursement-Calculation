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