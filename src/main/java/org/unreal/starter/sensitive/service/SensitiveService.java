package org.unreal.starter.sensitive.service;

import org.springframework.stereotype.Service;
import org.unreal.starter.sensitive.annoations.SensitiveStrategy;
import org.unreal.starter.sensitive.properties.SensitiveProperties;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensitiveService {

    private final SensitiveProperties sensitiveProperties;

    public SensitiveService(SensitiveProperties sensitiveProperties) {
        this.sensitiveProperties = sensitiveProperties;
    }

    public List<String> getRules() {
        if (sensitiveProperties == null || (sensitiveProperties != null && sensitiveProperties.getRules() == null)){
            return null;
        }
        return sensitiveProperties.getRules().stream().map(r -> r.getField()).collect(Collectors.toList());
    }

    public String maskDataByFieldName(String fieldName, String value) {
        if (value == null){
            return null;
        }
        for (SensitiveProperties.Rule rule : sensitiveProperties.getRules()) {
            if (fieldName.equals(rule.getField())) {
                return value.replaceAll(rule.getRegex(), rule.getReplacement());
            }
        }
        return value;
    }

    public String maskDataByAnnotation(SensitiveStrategy strategy, String fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return fieldValue.replaceAll(strategy.getRegex(), strategy.getReplacement());
    }
}


//package org.unreal.starter.sensitive.service;
//
//import org.springframework.stereotype.Service;
//import org.unreal.starter.sensitive.annoations.SensitiveStrategy;
//import org.unreal.starter.sensitive.properties.SensitiveProperties;
//
//@Service
//public class SensitiveService {
//
//    private final SensitiveProperties sensitiveProperties;
//
//    public SensitiveService(SensitiveProperties sensitiveProperties) {
//        this.sensitiveProperties = sensitiveProperties;
//    }
//
//    public String maskDataByFieldName(String fieldName, String value) {
//        if (value == null){
//            return null;
//        }
//        for (SensitiveProperties.Rule rule : sensitiveProperties.getRules()) {
//            if (fieldName.equals(rule.getField())) {
//                return value.replaceAll(rule.getRegex(), rule.getReplacement());
//            }
//        }
//        return value;
//    }
//
//    public String maskDataByAnnotation(SensitiveStrategy strategy, String fieldValue) {
//        if (fieldValue == null) {
//            return null;
//        }
//        return fieldValue.replaceAll(strategy.getRegex(), strategy.getReplacement());
//    }
//}
