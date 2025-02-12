# Project Introduction and User Manual

## Project Introduction

This project is a tool for processing sensitive information in data, primarily used in Java applications to mask sensitive data. It is based on the Spring Boot framework and provides a series of annotations and configurations to simplify the handling of sensitive data.

## Features

- **Annotation-based Marking**: Supports marking sensitive fields with annotations to simplify the handling of sensitive information in code.
- **Custom Masking Rules**: Allows users to define their own masking rules to meet different business requirements.
- **Configuration File Support**: Supports defining masking rules through configuration files.
- **Integration with Jackson**: Automatically masks data during JSON serialization.

## User Manual

### 1. Adding Dependencies

Add the following dependencies to your `pom.xml` file:

For JDK 1.8:
```xml
<dependency>
    <groupId>io.github.lincoln-cn</groupId>
    <artifactId>sensitive-starter</artifactId>
    <version>1.0</version>
</dependency>
```

For JDK 17:
```xml
<dependency>
    <groupId>io.github.lincoln-cn</groupId>
    <artifactId>sensitive-starter</artifactId>
    <version>1.0</version>
</dependency>
```

### 2. Using Annotations

You can mark sensitive fields in your Java classes with custom annotations provided by this library. For example:

```java
import io.github.lincoln-cn.sensitive.annotation.Sensitive;
import io.github.lincoln-cn.sensitive.enums.MaskStrategy;

public class User {
    @Sensitive(strategy = MaskStrategy.PARTIAL)
    private String phoneNumber;

    @Sensitive(strategy = MaskStrategy.FULL)
    private String idCardNumber;

    // Getters and Setters
}
```

### 3. Custom Masking Rules

You can define your own masking rules by creating a custom strategy. For example:

```java
import io.github.lincoln-cn.sensitive.MaskStrategy;

public class CustomMaskStrategy implements MaskStrategy {
    @Override
    public String mask(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(".", "*");
    }
}
```

Then, you can use this custom strategy in your annotations:

```java
@Sensitive(strategy = CustomMaskStrategy.class)
private String customSensitiveField;
```

### 4. Configuration File

You can also define masking rules in a configuration file. For example, in your `application.yml`:

```yaml
sensitive:
  rules:
    - field: phoneNumber
      strategy: PARTIAL
    - field: idCardNumber
      strategy: FULL
```

### 5. Integration with Jackson

When serializing objects to JSON, the library will automatically apply the masking rules defined through annotations or configuration files. For example:

```java
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    public static void main(String[] args) throws Exception {
        User user = new User();
        user.setPhoneNumber("1234567890");
        user.setIdCardNumber("123456789012345678");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);
        System.out.println(json);
    }
}
```

This will output a JSON string with the sensitive fields masked according to the defined rules.

### 6. Additional Notes

- **Version Compatibility**: Ensure that the version of the `sensitive-starter` dependency matches your project's requirements.
- **Performance Considerations**: Masking operations may introduce some performance overhead. Test thoroughly in your environment to ensure it meets your performance needs.

