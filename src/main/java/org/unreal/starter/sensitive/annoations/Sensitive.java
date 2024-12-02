package org.unreal.starter.sensitive.annoations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
public @interface Sensitive {
    SensitiveStrategy strategy() default SensitiveStrategy.MASK_ALL;
}