--liquibase formatted sql

CREATE TABLE Category (
                          id UUID NOT NULL,
                          name VARCHAR(255),
                          parent_id UUID,
                          author_id UUID,
                          created_at TIMESTAMP,
                          updated_at TIMESTAMP,
                          PRIMARY KEY (id),
                          FOREIGN KEY (parent_id) REFERENCES Category(id) ON DELETE CASCADE
);

CREATE TABLE Topic (
                       id UUID NOT NULL,
                       name VARCHAR(255),
                       description VARCHAR(255),
                       category_id UUID,
                       author_id UUID,
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP,
                       PRIMARY KEY (id),
                       FOREIGN KEY (category_id) REFERENCES Category(id) ON DELETE CASCADE
);

CREATE TABLE Message (
                         id UUID NOT NULL,
                         text VARCHAR(255),
                         topic_id UUID,
                         author_id UUID,
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP,
                         PRIMARY KEY (id),
                         FOREIGN KEY (topic_id) REFERENCES Topic(id) ON DELETE CASCADE
);
