package org.unreal.starter.sensitive.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Configuration;
import org.unreal.starter.sensitive.service.SensitiveService;
import org.springframework.context.annotation.Bean;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;

@Configuration
public class JacksonConfig {

    @Resource
    private SensitiveService sensitiveService;

    @Bean
    public SimpleModule sensitiveModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new SensitiveJsonSerializer(sensitiveService));
        return module;
    }

    public static class SensitiveJsonSerializer extends JsonSerializer<String> {

        private final SensitiveService sensitiveService;

        public SensitiveJsonSerializer(SensitiveService sensitiveService) {
            this.sensitiveService = sensitiveService;
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeString("");
                return;
            }

            // 假设字段名是 "email" 或 "phone"
            String fieldName = gen.getOutputContext().getCurrentName();

            Object object = gen.getOutputContext().getCurrentValue();
            Class<?> objectClass = object.getClass();
            Field field = getField(objectClass, fieldName);

            if (field.getName().equals(fieldName)) {
                gen.writeString(sensitiveService.maskDataByFieldName(field.getName(), value));
            } else {
                gen.writeString(value);
            }
        }
    }

    public static Field getField(Class<?> clazz,  String fieldName) {
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
}
