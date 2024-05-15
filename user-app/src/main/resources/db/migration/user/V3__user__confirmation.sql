ALTER TABLE users
    ADD is_confirmed BOOLEAN NOT NULL DEFAULT false;

CREATE TABLE user_confirmation(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    confirmation_code UUID NOT NULL
)