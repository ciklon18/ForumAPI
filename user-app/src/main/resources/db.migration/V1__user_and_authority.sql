CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL
);

CREATE TABLE authorities (
     id UUID PRIMARY KEY,
     user_id UUID REFERENCES users(id),
     authority_type VARCHAR(255)
);


