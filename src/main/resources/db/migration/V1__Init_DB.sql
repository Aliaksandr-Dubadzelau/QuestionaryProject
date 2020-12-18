drop table if exists test_result cascade;
drop table if exists test_specialization cascade;
drop table if exists user_role cascade;
drop table if exists users cascade;
drop sequence if exists hibernate_sequence;

create sequence hibernate_sequence start 1 increment 1;

create table answer (
    id int8 not null,
    answer varchar(2048),
    question_id int8,
    test_result_id int8,
    primary key (id)
                    );
create table question (
    id int8 not null,
    obligatory_answer boolean not null,
    page_number int4 not null,
    question_number int4 not null,
    question_title varchar(500) not null,
    question_type varchar(255) not null,
    test_id int8,
    primary key (id)
                      );

create table test (
    id int8 not null,
    date_of_change date not null,
    title varchar(30) not null,
    creator_id int8,
    primary key (id)
                  );

create table test_result (
    id int8 not null,
    completed boolean not null,
    test_id int8,
    user_id int8,
    primary key (id)
                         );

create table test_specialization (
    id int8 not null,
    anonymous boolean not null,
    indicator boolean not null,
    numerated_page boolean not null,
    numerated_question boolean not null,
    obligatory_answer boolean not null,
    random_question_sequence boolean not null,
    test_id int8,
    primary key (id)
                                 );

create table user_role (
    user_id int8 not null,
    roles varchar(255) not null,
    primary key (user_id, roles)
                       );

create table users (
    id int8 not null,
    activated boolean not null,
    activation_code varchar(255),
    email varchar(70) not null,
    name varchar(50) not null,
    password varchar(50) not null,
    password_code int4 not null,
    registration_date date not null,
    primary key (id)
                   );

alter table if exists users
    add constraint user_activation_code_unique
        unique (activation_code);

alter table if exists users
    add constraint user_email_unique
        unique (email);

alter table if exists users
    add constraint user_name_unique
        unique (name);

alter table if exists answer
    add constraint answer_question_fk
        foreign key (question_id)
            references question;

alter table if exists answer
    add constraint answer_test_result_fk
        foreign key (test_result_id)
            references test_result;

alter table if exists question
    add constraint question_test_fk
        foreign key (test_id)
            references test;

alter table if exists test
    add constraint test_user_fk
        foreign key (creator_id)
            references users;

alter table if exists test_result
    add constraint test_result_test_fk
        foreign key (test_id)
            references test;

alter table if exists test_result
    add constraint test_result_user_fk
        foreign key (user_id)
            references users;

alter table if exists test_specialization
    add constraint test_specialization_test_fk
        foreign key (test_id)
            references test;

alter table if exists user_role
    add constraint user_role_user_fk
        foreign key (user_id)
            references users;

