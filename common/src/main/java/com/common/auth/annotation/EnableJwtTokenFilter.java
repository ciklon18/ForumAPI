package com.common.auth.annotation;

import com.common.auth.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JwtTokenFilter.class)
public @interface EnableJwtTokenFilter {
}
