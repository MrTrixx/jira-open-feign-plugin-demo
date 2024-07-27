package ru.jira.open.feign.plugin.demo.spring.context;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface FeignClient {

    String value();
    String url();
    Class<?>[] config() default {};

    boolean autoConfigure() default false;

}
