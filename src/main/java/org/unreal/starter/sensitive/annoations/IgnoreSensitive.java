package org.unreal.starter.sensitive.annoations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface IgnoreSensitive {

    /**
     * Ignore {@link Sensitive} field names.
     *
     * @return field names
     */
    String[] value() default {};
}