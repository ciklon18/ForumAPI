CREATE TABLE authority
(
    id      UUID primary key DEFAULT gen_random_uuid(),
    user_id UUID,
    role    VARCHAR(255)
);

CREATE TABLE moderator
(
    id          UUID primary key DEFAULT gen_random_uuid(),
    user_id     UUID,
    category_id UUID
)