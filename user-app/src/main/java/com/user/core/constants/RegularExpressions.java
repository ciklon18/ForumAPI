package com.user.core.constants;

public class RegularExpressions {
    public static final String EMAIL = "([a-zA-Z0-9]((?:(?!(\\.{2,}|\\.[-_]))[a-zA-Z0-9._-]){0,62}[a-zA-Z0-9])?)@([a-zA-Z0-9]((?:(?!(\\.{2,}|\\.-|-{2,}))[a-zA-Z0-9.-]){0,253}[a-zA-Z0-9])?)(\\.[a-zA-Z]+)$";
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    public static final String PHONE = "^((\\+7)||8)\\d{10}$";
}
