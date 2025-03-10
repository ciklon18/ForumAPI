CREATE TABLE topic_subscription(
    id uuid PRIMARY KEY,
    user_id uuid,
    topic_id uuid,
    created_at TIMESTAMP,
    CONSTRAINT unique_user_topic_pair UNIQUE (user_id, topic_id)
)