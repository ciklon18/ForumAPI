create table users (
    id uuid primary key,
    email varchar(100) not null,
    login varchar(100) not null unique,
    password varchar(100) not null,
    name varchar(100) not null,
    surname varchar(100) not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    deleted_at timestamp,
    blocked_at timestamp
);

