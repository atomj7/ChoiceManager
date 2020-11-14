create sequence hibernate_sequence start 1 increment 1;
create table usr
(
    id              int8    not null,
    is_activated    boolean,
    activation_code varchar(255),
    active          boolean not null,
    email           varchar(255),
    gender          varchar(255),
    google_auth_id  varchar(255),
    last_visit      timestamp,
    locale          varchar(255),
    login           varchar(255),
    name            varchar(255),
    password        varchar(255),
    surname         varchar(255),
    user_pic        varchar(255),
    provider        varchar(255),
    provider_id     varchar(255),
    primary key (id)
);

create table categories
(
    id       int8         not null,
    category varchar(255) not null,
    primary key (id)
);

create table questions
(
    id          int8         not null,
    question    varchar(255) not null,
    type        varchar(255) not null,
    category_id int8         not null,
    primary key (id)
);
create table answers
(
    id          bigint       not null
        constraint answers_pkey primary key,
    value       varchar(255) not null,
    question_id bigint       not null,
    user_id     bigint       not null
);

create table roles
(
    id   int8 not null,
    name varchar(255),
    primary key (id)
);


create table usr_roles
(
    users_id int8 not null,
    roles_id int8 not null,
    primary key (users_id, roles_id)
);

create table goals
(
    id          int8 not null,
    category    varchar(255),
    explanation varchar(255),
    name        varchar(255),
    primary key (id)
);

alter table if exists usr
    add constraint usr_email_uk unique (email);

alter table if exists usr
    add constraint usr_login_uk unique (login);

alter table if exists answers
    add constraint answers_question_fk foreign key (question_id) references questions;

alter table if exists answers
    add constraint user_id_usr_fk foreign key (user_id) references usr;

alter table if exists questions
    add constraint questions_categories_fk foreign key (category_id) references categories;

alter table if exists usr_roles
    add constraint usr_roles_roles_fk foreign key (roles_id) references roles;

alter table if exists usr_roles
    add constraint usr_roles_usr_fk foreign key (users_id) references usr;
