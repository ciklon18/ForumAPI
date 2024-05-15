package com.user.api.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ApiPaths {

    public static final String LOGIN = "/api/user/login";

    public static final String REGISTER = "/api/user/register";

    public static final String LOGOUT = "/api/user/logout";

    public static final String REFRESH = "/api/user/refresh";

    public static final String ACCESS = "/api/user/access";

    public static final String CONFIRMATION = "/api/user/confirm/{confirmation_code}";

    public static final String ASSIGN_ROLE = "/api/admin/authority/user/{id}/assign";

    public static final String REMOVE_ROLE = "/api/admin/authority/user/{id}/remove";

    public static final String CREATE_USER = "/api/admin/user/create";

    public static final String UPDATE_USER = "/api/admin/user/{id}/update";

    public static final String DELETE_USER = "/api/admin/user/{id}/delete";

    public static final String BLOCK_USER = "/api/admin/user/{id}/block";
}
