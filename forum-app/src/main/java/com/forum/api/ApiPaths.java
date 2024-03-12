package com.forum.api;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiPaths {

    public static final String CATEGORY = "/api/category";
    public static final String CATEGORY_HIERARCHY = "/api/category/hierarchy";
    public static final String CATEGORY_BY_ID = "/api/category/{categoryId}";
    public static final String CATEGORY_BY_QUERY = "/api/category/query";
    public static final String TOPIC = "/api/topic";
    public static final String TOPIC_BY_ID = "/api/topic/{topicId}";
    public static final String TOPIC_BY_QUERY = "/api/topic/query";
    public static final String MESSAGE = "/api/message";
    public static final String MESSAGE_BY_ID = "/api/message/{messageId}";
    public static final String MESSAGE_BY_QUERY = "/api/message/query";
    public static final String MESSAGE_BY_TOPIC_ID = "/api/message/topic/{topicId}";
}
