package org.unreal.starter.sensitive.annoations;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.unreal.starter.sensitive.configuration.JacksonSensitiveSerializer;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
@JsonSerialize(using = JacksonSensitiveSerializer.class)
public @interface Sensitive {
    SensitiveStrategy strategy() default SensitiveStrategy.MASK_ALL;
}