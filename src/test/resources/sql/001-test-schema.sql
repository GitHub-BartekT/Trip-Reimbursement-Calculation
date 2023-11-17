drop table if exists USERS;
drop table if exists USER_GROUPS;

create table USER_GROUPS (
                             id int primary key auto_increment,
                             name varchar(100) not null
);

create table USERS (
                       id int primary key auto_increment,
                       name varchar(100) not null ,
                       user_group_id int null,
                       foreign key (user_group_id) references USER_GROUPS (id)
);