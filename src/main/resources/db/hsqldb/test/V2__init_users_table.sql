create table USERS(
                      id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
                      name varchar(100) not null,
                      user_group_id int null,
                      foreign key (user_group_id) references USER_GROUPS (id)
);
