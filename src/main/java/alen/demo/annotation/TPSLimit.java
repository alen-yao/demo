package alen.demo.annotation;

import java.lang.annotation.*;

/**
 * @Author: Alen
 * @Date: 2022/2/9
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TPSLimit {
    long time() default 1000;
    int count() default 100;
}
