package org.unreal.starter.sensitive.configuration;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unreal.starter.sensitive.annoations.IgnoreSensitive;
import org.unreal.starter.sensitive.annoations.Sensitive;
import org.unreal.starter.sensitive.service.SensitiveService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;


public class JacksonSensitiveSerializer extends JsonSerializer<String> {

    private static final Logger log = LoggerFactory.getLogger(JacksonSensitiveSerializer.class);

    private final SensitiveService sensitiveService;

    private List<String> rulesCache;


    @JsonCreator
    public JacksonSensitiveSerializer(@JsonProperty("sensitiveService") SensitiveService sensitiveService) {
        this.sensitiveService = sensitiveService;
        this.rulesCache = sensitiveService != null ? sensitiveService.getRules() : null;
    }

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Class<?> currentClass = clazz;
        while (currentClass != null) {
            try {
                Field field = currentClass.getDeclaredField(fieldName);
                if (field != null) {
                    field.setAccessible(true);
                    return field;
                }
            } catch (NoSuchFieldException e) {
                // 继续在父类中查找
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    @Override
    public void serialize(String fieldValue, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        if (Objects.isNull(fieldValue)) {
            log.debug("Skip sensitive, because value is null.");
            gen.writeNull();
            return;
        }

        String fieldName = gen.getOutputContext().getCurrentName();
        Object object = gen.getOutputContext().getCurrentValue();
        Class<?> objectClass = object.getClass();
        Field field = getField(objectClass, fieldName);

        if (Objects.isNull(field)) {
            log.debug("Field {} not found in class {}", fieldName, objectClass.getName());
            gen.writeString(fieldValue);
            return;
        }

        // Check if the class or field is annotated with @IgnoreSensitive
        if (objectClass.isAnnotationPresent(IgnoreSensitive.class) || field.isAnnotationPresent(IgnoreSensitive.class)) {
            log.debug("Skip sensitive for {}, because @IgnoreSensitive is present.", fieldName);
            gen.writeString(fieldValue);
            return;
        }

        if (rulesCache != null && rulesCache.contains(fieldName)) {
            log.debug("Apply sensitive for {}, because it is in the rules list.", fieldName);
            gen.writeString(sensitiveService.maskDataByFieldName(fieldName, fieldValue));
        } else {
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            if (sensitive != null) {
                log.debug("Apply sensitive for {}, because @Sensitive is present.", fieldName);
                gen.writeString(sensitiveService.maskDataByAnnotation(sensitive.strategy(), fieldValue));
            } else {
                log.debug("No sensitive rule found for {}. Writing original value.", fieldName);
                gen.writeString(fieldValue);
            }
        }
    }
}