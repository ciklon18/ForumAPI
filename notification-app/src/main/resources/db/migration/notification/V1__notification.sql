CREATE TABLE notification(
    id UUID PRIMARY KEY,
    header TEXT NOT NULL,
    text TEXT,
    notification_status TEXT NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL
)