package com.forum.api.constant;

public class IntegrationPaths {
    public static final String CATEGORY_BY_ID = "/integration/category/{id}";

    public static final String MESSAGE_BY_ID_USER_ID = "/integration/message/{messageId}/user/{userId}/owner";

    public static final String IS_MODERATOR_BY_MESSAGE_ID = "/integration/message/{messageId}/moderator";
}
