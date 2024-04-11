package com.common.security.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IntegrationPaths {
    public static final String CATEGORY_BY_ID = "/integration/category/{id}";

    public static final String MESSAGE_BY_ID_USER_ID = "/integration/message/{messageId}/user/{userId}/owner";

    public static final String IS_MODERATOR_BY_MESSAGE_ID = "/integration/message/{messageId}/moderator";

    public static final String CHECK_USER_BY_ID = "/integration/user/check/{id}";

    public static final String USER_BY_ID = "/integration/user/{id}";

    public static final String MODERATOR_CATEGORY_BY_USER_ID = "/integration/user/{userId}/moderator/category";
}
