package org.unreal.starter.sensitive.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.unreal.starter.sensitive.properties.SensitiveProperties;
import org.unreal.starter.sensitive.resolver.RequestMappingResolver;
import org.unreal.starter.sensitive.service.SensitiveService;

@Configuration
@EnableConfigurationProperties({SensitiveProperties.class})
public class SensitiveAutoConfiguration {

    private final JacksonSensitiveSerializer sensitiveSerializer;

    public SensitiveAutoConfiguration(JacksonSensitiveSerializer sensitiveSerializer){
        this.sensitiveSerializer = sensitiveSerializer;
    }

    @Bean
    public SensitiveService dataMaskingService(SensitiveProperties sensitiveProperties) {
        return new SensitiveService(sensitiveProperties);
    }

    @Bean
    public ObjectMapper dataMaskingObjectMapper(SensitiveService sensitiveService) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, sensitiveSerializer);
        objectMapper.registerModule(module);
        return objectMapper;
    }
}