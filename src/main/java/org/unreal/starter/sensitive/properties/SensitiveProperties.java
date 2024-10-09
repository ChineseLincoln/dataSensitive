package org.unreal.starter.sensitive.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "data-masking")
@Component
public class SensitiveProperties {

    private List<Rule> rules = new ArrayList<>();

    public static class Rule {
        private String field;
        private String regex;
        private String replacement;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getRegex() {
            return regex;
        }

        public void setRegex(String regex) {
            this.regex = regex;
        }

        public String getReplacement() {
            return replacement;
        }

        public void setReplacement(String replacement) {
            this.replacement = replacement;
        }
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

}
