package com.user.api.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiPaths {

    public static final String LOGIN = "/api/user/login";

    public static final String REGISTER = "/api/user/register";

    public static final String LOGOUT = "/api/user/logout";

    public static final String CHECK_USER_BY_ID = "/api/user/check/{id}";

    public static final String USER_BY_ID = "/api/user/{id}";

    public static final String REFRESH = "/api/user/refresh";

    public static final String ACCESS = "/api/user/access";
}
