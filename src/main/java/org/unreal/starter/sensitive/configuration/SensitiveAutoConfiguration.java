package org.unreal.starter.sensitive.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.unreal.starter.sensitive.properties.SensitiveProperties;
import org.unreal.starter.sensitive.service.SensitiveService;


@Configuration
@ConditionalOnProperty(prefix = "data-masking", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({SensitiveProperties.class})
public class SensitiveAutoConfiguration {

    private final SensitiveService sensitiveService;


    @Autowired
    public SensitiveAutoConfiguration(@Lazy SensitiveService sensitiveService) {
        this.sensitiveService = sensitiveService;
    }

    @Bean
    public SensitiveService dataMaskingService(SensitiveProperties sensitiveProperties) {
        return new SensitiveService(sensitiveProperties);
    }

    @Bean
    public SimpleModule sensitiveModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new JacksonSensitiveSerializer(sensitiveService));
        return module;
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public ObjectMapper dataMaskingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(sensitiveModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

}
