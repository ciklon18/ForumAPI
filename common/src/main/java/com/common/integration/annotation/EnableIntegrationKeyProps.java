package com.common.integration.annotation;

import com.common.integration.props.IntegrationKeyProps;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(IntegrationKeyProps.class)
public @interface EnableIntegrationKeyProps {
}
