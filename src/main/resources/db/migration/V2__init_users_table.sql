create table USERS(
                      id int primary key auto_increment,
                      name varchar(100) not null,
                      max_value double default (0)
);
alter table USER_GROUPS add column USER_GROUP_ID int null;
alter table USER_GROUPS
    add foreign key (USER_GROUP_ID) references USER_GROUPS (id);