package org.unreal.starter.sensitive.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.unreal.starter.sensitive.properties.SensitiveProperties;
import org.unreal.starter.sensitive.service.SensitiveService;

@AutoConfiguration
@ConditionalOnMissingBean(SensitiveService.class)
@EnableConfigurationProperties({SensitiveProperties.class})
public class SensitiveAutoConfiguration {

    private final JacksonSensitiveSerializer sensitiveSerializer;

    public SensitiveAutoConfiguration(JacksonSensitiveSerializer sensitiveSerializer){
        this.sensitiveSerializer = sensitiveSerializer;
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveService dataMaskingService(SensitiveProperties sensitiveProperties) {
        return new SensitiveService(sensitiveProperties);
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    @ConditionalOnMissingBean(name = "objectMapper")
    public ObjectMapper objectMapper(SensitiveService sensitiveService) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, sensitiveSerializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }
}