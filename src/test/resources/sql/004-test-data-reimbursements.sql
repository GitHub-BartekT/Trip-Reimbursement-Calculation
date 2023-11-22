insert into USER_GROUPS (name) values
                                   ('fooGroup'),
                                   ('barGroup'),
                                   ('foobarGroup');
insert into USERS (name, user_group_id) values
                                            ('foo', 1),
                                            ('bar', 1),
                                            ('foobar', 1),
                                            ('foo', 2),
                                            ('bar', 2),
                                            ('foobar', 2),
                                            ('foo', 3),
                                            ('bar', 3),
                                            ('foobar', 3);
