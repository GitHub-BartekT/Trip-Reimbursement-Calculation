drop table if exists USER_GROUPS;
create table USER_GROUPS(
                      id int primary key auto_increment,
                      name varchar(100) not null
)