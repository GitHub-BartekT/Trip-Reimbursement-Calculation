create table USER_GROUPS(
                      id INT GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
                      name varchar(100) not null
)