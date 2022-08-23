package com.rjgf.threecost.crawdata.util.aspect;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestTimeAnnotation {
    String value() default "";
}
