package com.forum.api.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiPaths {
    public static final String CATEGORY = "/api/forum/category";
    public static final String CATEGORY_HIERARCHY = "/api/forum/category/hierarchy";
    public static final String CATEGORY_BY_ID = "/api/forum/category/{categoryId}";
    public static final String CATEGORY_BY_QUERY = "/api/forum/category/query";
    public static final String TOPIC = "/api/forum/topic";
    public static final String TOPIC_BY_ID = "/api/forum/topic/{topicId}";

    public static final String TOPIC_BY_ID_SUBSCRIBE = "/api/forum/topic/{topicId}/subscribe";

    public static final String TOPIC_BY_ID_UNSUBSCRIBE = "/api/forum/topic/{topicId}/unsubscribe";

    public static final String TOPIC_BY_QUERY = "/api/forum/topic/query";
    public static final String MESSAGE = "/api/forum/message";
    public static final String MESSAGE_BY_ID = "/api/forum/message/{messageId}";
    public static final String MESSAGE_BY_QUERY = "/api/forum/message/query";
    public static final String MESSAGE_BY_TOPIC_ID = "/api/forum/message/topic/{topicId}";
}
