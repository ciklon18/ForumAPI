create table users (
    id uuid primary key,
    email varchar(100) not null unique,
    login varchar(100) not null unique,
    password varchar(100) not null,
    name varchar(100) not null,
    surname varchar(100) not null
);

