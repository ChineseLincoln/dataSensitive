//package org.unreal.starter.sensitive.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.unreal.starter.sensitive.properties.SensitiveProperties;
//import org.unreal.starter.sensitive.service.SensitiveService;
//
//@Configuration
//@ImportAutoConfiguration(SensitiveProperties.class)
//public class SensitiveAutoConfiguration {
//    private static final Logger logger = LoggerFactory.getLogger(SensitiveAutoConfiguration.class);
//
//    @Bean
//    public SensitiveService dataMaskingService(SensitiveProperties sensitiveProperties) {
//        return new SensitiveService(sensitiveProperties);
//    }
//
//    @Bean
//    @ConditionalOnBean(ObjectMapper.class)
//    public ObjectMapper dataMaskingObjectMapper(SensitiveService sensitiveService) {
//        logger.info("dataMaskingObjectMapper init");
//        ObjectMapper objectMapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addSerializer(String.class, new JacksonSensitiveSerializer(sensitiveService));
//        objectMapper.registerModule(module);
//        return objectMapper;
//    }
//}
package org.unreal.starter.sensitive.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.unreal.starter.sensitive.properties.SensitiveProperties;
import org.unreal.starter.sensitive.service.SensitiveService;

@Configuration
@EnableConfigurationProperties({SensitiveProperties.class})
public class SensitiveAutoConfiguration {


    @Bean
    public SensitiveService dataMaskingService(SensitiveProperties sensitiveProperties) {
        return new SensitiveService(sensitiveProperties);
    }

    @Bean
    @ConditionalOnBean(ObjectMapper.class)
    public ObjectMapper dataMaskingObjectMapper(SensitiveService sensitiveService) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(String.class, new JacksonSensitiveSerializer(sensitiveService));
        objectMapper.registerModule(module);
        return objectMapper;
    }


}