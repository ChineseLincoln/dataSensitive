package org.unreal.starter.sensitive.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.unreal.starter.sensitive.annoations.IgnoreSensitive;
import org.unreal.starter.sensitive.annoations.Sensitive;
import org.unreal.starter.sensitive.service.SensitiveService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;


@Component
public class JacksonSensitiveSerializer extends JsonSerializer<String> {

    private static final Logger log = LoggerFactory.getLogger(JacksonSensitiveSerializer.class);

    private SensitiveService sensitiveService;

    public JacksonSensitiveSerializer(SensitiveService sensitiveService) {
        this.sensitiveService = sensitiveService;
    }


    @Override
    public Class<String> handledType() {
        return String.class;
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
         // Check if the class or field is annotated with @IgnoreSensitive
        if (objectClass.isAnnotationPresent(IgnoreSensitive.class) || 
            getField(objectClass, fieldName).isAnnotationPresent(IgnoreSensitive.class)) {
            log.debug("Skip sensitive for {}, because @IgnoreSensitive is present.", fieldName);
            gen.writeString(fieldValue);
            return;
        }
        if (objectClass.isAnnotationPresent(Sensitive.class)) {
            log.debug("Skip sensitive for {}, because @Sensitive is null.", fieldName);
            gen.writeString(sensitiveService.maskDataByFieldName(fieldName , fieldValue));

        }else{
            Sensitive sensitive = objectClass.getAnnotation(Sensitive.class);
            log.debug("apply sensitive for {}, because @Sensitive is not null.", fieldName);
            gen.writeString(sensitiveService.maskDataByAnnotation(sensitive.strategy() , fieldValue));
        }
    }

    public Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            log.error("Field not found: {}", fieldName, e);
            return null;
        }
    }
}
