package com.user.api.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IntegrationPaths {

    public static final String CHECK_USER_BY_ID = "/integration/user/check/{id}";

    public static final String USER_BY_ID = "/integration/user/{id}";

    public static final String MODERATOR_CATEGORY_BY_USER_ID = "/integration/user/{userId}/moderator/category";

    public static final String USER_EMAIL = "/integration/user/email";
}
