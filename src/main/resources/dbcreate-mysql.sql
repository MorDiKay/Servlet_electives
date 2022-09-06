set foreign_key_checks=0;
drop table if exists roles;
set foreign_key_checks=1;

create table if not exists roles
(
    id int,
    name varchar(45) unique not null,
    primary key (id)
    );

insert into roles VALUES (0, 'admin');
insert into roles VALUES (1, 'client');
insert into roles VALUES (2, 'lecturer');


set foreign_key_checks=0;
drop table if exists users;
set foreign_key_checks=1;

create table if not exists users
(
    id int,
    name varchar(45) default 'user' not null unique,
    password varchar(45) not null,
    role_id int not null,
    foreign key (role_id)
    references roles(id)
    on delete cascade on update no action,
    is_blocked boolean default false,
    primary key (id)
    );

insert into users values (0,'admin', 'admin', 0, default);
insert into users values (1,'mordi', 'admin', 1, default);
insert into users values (2,'lecturer', 'admin', 2, default);
insert into users values (3,'lecturer2', 'admin', 2, default);


set foreign_key_checks=0;
drop table  if exists topics;
set foreign_key_checks=1;

create table if not exists topics
(
    id int,
    name varchar(45) unique not null,
    primary key (id)
    );

insert into topics values (0, 'sport');
insert into topics values (1, 'programming');

set foreign_key_checks=0;
drop table if exists electives;
set foreign_key_checks=1;
create table if not exists electives
(
    id int,
    name varchar(45) unique not null,
    start_date date not null,
    end_date date not null,
    status varchar(30) not null,
    topic_id int not null,
    lecturer_id int,
    index topic_id_idx (topic_id ASC),
    constraint topic_id
    foreign key (topic_id)
    references topics (id)
    on delete cascade on update no action ,
    foreign key (lecturer_id)
    references users (id)
    on delete no action on update no action,
    student_count tinyint,
    primary key (id)
    );

insert into electives values (
                                 0, 'Basketball', 20210615, 20210720, 'ended', 0, 2, 15
                             );
insert into electives values (
                                 1, 'Judo', 20180615, 20200615, 'ended', 0, 2, 15
                             );
insert into electives values (
                                 2, 'Sambo', 20220901, 20230901, 'expected', 0, 0, 0
                             );
insert into electives values (
                                 3, 'Football', 20180901, 20220901, 'in progress', 0, 2, 20
                             );
insert into electives values (
                                 4, 'Volleyball', 20180901, 20220901, 'in progress', 0, 2, 25
                             );
insert into electives values (
                                 5, 'Tennis', 20180109, 20220901, 'in progress', 0, 2, 14
                             );
insert into electives values (
                                 6, 'Java Summer 2021', 20210623, 20210916, 'ended', 1, 2, 50
                             );
insert into electives values (
                                 7, 'DevOps', 20200901, 20210531, 'in progress', 1, 2, 15
                             );
insert into electives values (
                                 8, 'C# Spring 2022', 20220312, 20220526, 'expected', 1, 0, 0
                             );
insert into electives values (
                                 9, 'JavaScript for students', 20210901, 20220831, 'in progress', 1, 2, 10
                             );
insert into electives values (
                                 10, 'C++ OOP', 20220901, 20230901, 'expected', 1, 0, 0
                             );

drop table if exists electives_have_users;

create table if not exists electives_have_users
(
    user_id int not null,
    foreign key (user_id)
    references users(id)
    on delete cascade on update no action ,
    elective_id int not null,
    foreign key (elective_id)
    references electives(id)
    on delete cascade on update no action,
    mark int
    );

insert into electives_have_users values (
                                            1, 0, 0
                                        );