CREATE TABLE authority
(
    id             UUID PRIMARY KEY,
    user_id        UUID REFERENCES users (id),
    authority_type VARCHAR(255)
);

CREATE TABLE moderator
(
    id          UUID primary key DEFAULT gen_random_uuid(),
    user_id     UUID,
    category_id UUID,
    foreign key (user_id) references users (id),
    foreign key (category_id) references category (id)
)