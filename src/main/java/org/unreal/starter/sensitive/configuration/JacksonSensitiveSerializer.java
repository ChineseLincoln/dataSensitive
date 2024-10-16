package org.unreal.starter.sensitive.configuration;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unreal.starter.sensitive.annoations.IgnoreSensitive;
import org.unreal.starter.sensitive.annoations.Sensitive;
import org.unreal.starter.sensitive.properties.SensitiveProperties;
import org.unreal.starter.sensitive.service.SensitiveService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Component
public class JacksonSensitiveSerializer extends JsonSerializer<String> {

    private static final Logger log = LoggerFactory.getLogger(JacksonSensitiveSerializer.class);

    private SensitiveService sensitiveService;

    @Autowired
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

        List<String> rules = null;
        if(sensitiveService != null) {
            rules = sensitiveService.getRules();
        }
        // Check if the field is annotated with @Sensitive
        if (rules.contains(fieldName)) {
            log.debug("Skip sensitive for {}, because @Sensitive is null.", fieldName);
            gen.writeString(sensitiveService.maskDataByFieldName(fieldName , fieldValue));
        } else {
            if(sensitiveService == null){
                sensitiveService = new SensitiveService(new SensitiveProperties());
            }
            Sensitive sensitive = field.getAnnotation(Sensitive.class);
            log.debug("apply sensitive for {}, because @Sensitive is not null.", fieldName);
            gen.writeString(sensitiveService.maskDataByAnnotation(sensitive.strategy(), fieldValue));
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

//    public Field getField(Class<?> clazz, String fieldName) {
//        try {
//            Field field = clazz.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            return field;
//        } catch (NoSuchFieldException e) {
//            log.error("Field not found: {}", fieldName, e);
//            return null;
//        }
//    }
}
//package org.unreal.starter.sensitive.configuration;
//
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import jakarta.annotation.Resource;
//import lombok.NoArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.unreal.starter.sensitive.annoations.IgnoreSensitive;
//import org.unreal.starter.sensitive.annoations.Sensitive;
//import org.unreal.starter.sensitive.service.SensitiveService;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.Objects;
//
//
//@NoArgsConstructor
//@Component
//public class JacksonSensitiveSerializer extends JsonSerializer<String> {
//
//    private static final Logger log = LoggerFactory.getLogger(JacksonSensitiveSerializer.class);
//
//    @Resource
//    private SensitiveService sensitiveService;
//
//    @Autowired
//    public JacksonSensitiveSerializer(SensitiveService sensitiveService) {
//        this.sensitiveService = sensitiveService;
//    }
//
//    @Override
//    public Class<String> handledType() {
//        return String.class;
//    }
//
//    @Override
//    // 这段代码用于在序列化过程中对敏感数据进行脱敏处理。下面是加上注释后的代码：
//    public void serialize(String fieldValue, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
//        if (Objects.isNull(fieldValue)) {
//            // 如果值为空，则跳过脱敏
//            log.debug("Skip sensitive, because value is null.");
//            gen.writeNull();
//            return;
//        }
//
//        // 获取当前字段名称
//        String fieldName = gen.getOutputContext().getCurrentName();
//        // 获取当前对象
//        Object object = gen.getOutputContext().getCurrentValue();
//        // 获取当前对象类
//        Class<?> objectClass = object.getClass();
//        // 通过对象类和字段名称获取字段
//        Field field = getField(objectClass, fieldName);
//
//        // Check if the class or field is annotated with @IgnoreSensitive
//        if (objectClass.isAnnotationPresent(IgnoreSensitive.class) ||
//                getField(objectClass, fieldName).isAnnotationPresent(IgnoreSensitive.class)) {
//            // 如果类或字段上有 @IgnoreSensitive 注解，则跳过脱敏
//            log.debug("Skip sensitive for {}, because @IgnoreSensitive is present.", fieldName);
//            gen.writeString(fieldValue);
//            return;
//        }
//        // Check if the field is annotated with @Sensitive
//        if (field != null && field.isAnnotationPresent(Sensitive.class)) {
//            // 如果字段上有 @Sensitive 注解，则获取注解信息
//            Sensitive sensitive = field.getAnnotation(Sensitive.class);
//            if (sensitive == null) {
//                // 如果 @Sensitive 注解为空，则跳过脱敏
//                log.debug("Skip sensitive for {}, because @Sensitive is null.", fieldName);
//                gen.writeString(fieldValue);
//            } else {
//                // 如果 @Sensitive 注解不为空，则执行脱敏操作
//                log.debug("Apply sensitive for {}, because @Sensitive is not null.", fieldName);
//                gen.writeString(sensitiveService.maskDataByAnnotation(sensitive.strategy(), fieldValue));
//            }
//        } else {
//            // 如果字段上没有 @Sensitive 注解，则执行默认的脱敏操作
//            log.debug("Apply sensitive for {}, because @Sensitive is not null.", fieldName);
//            gen.writeString(sensitiveService.maskDataByFieldName(fieldName, fieldValue));
//        }
//
//    }
//
//
//
//
//    public Field getField(Class<?> clazz, String fieldName) {
//        try {
//            Field field = clazz.getDeclaredField(fieldName);
//            field.setAccessible(true);
//            return field;
//        } catch (NoSuchFieldException e) {
//            log.error("Field not found: {}", fieldName, e);
//            return null;
//        }
//    }
//}