package org.unreal.starter.sensitive.annoations;

public enum SensitiveStrategy {
    NAME("(.)(.*)", "$1****"),
    CHINESE_NAME("([\\u4e00-\\u9fa5])(\\u4e00-\\u9fa5+)", "$1**"),
    ENGLISH_NAME("([A-Za-z]+)\\s[A-Za-z]+\\s([A-Za-z]+)", "$1 *****"),
    MOBILE_PHONE("(\\d{3})\\d{4}(\\d{4})", "$1****$2"),
    EMAIL("(\\w)[^@]*(@\\w+\\.\\w+)", "$1****$2"),
    ID_CARD("(\\d{4})\\d{10}(\\d{4})", "$1****$2"),
    PASSWORD(".{8,}", "****"),
    ADDRESS("(\\S{3})\\S*(\\S{3})", "$1****$2"),
    LICENSE_PLATE("(\\S{2})\\S*(\\S{2})", "$1****$2"),
    SOCIAL_SECURITY_NUMBER("(\\d{3})\\d{2}(\\d{4})", "$1****$2"),
    BANK_CARD("(\\d{4})\\d{12}(\\d{4})", "$1****$2"),
    CREDIT_CARD("(\\d{4})\\d{8,12}(\\d{4})", "$1****$2"),
    BIRTHDAY("(\\d{2})\\d{2}(\\d{2})", "$1****$2"),
    MASK_ALL(".*", "****")
    // 其他策略...
    ;

    private final String regex;
    private final String replacement;

    SensitiveStrategy(String regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    public String getRegex() {
        return regex;
    }

    public String getReplacement() {
        return replacement;
    }
}
