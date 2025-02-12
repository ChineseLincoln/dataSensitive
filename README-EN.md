# English Introduction | [中文介绍](./README.md)
# Project Introduction and User Manual

## Project Introduction

This project is a data desensitization tool, primarily used for masking sensitive data within Java applications. It is based on the Spring Boot framework and provides a series of annotations and configurations to simplify the handling of sensitive data.

### Serialization Tool Requirements
**The serialization framework must use the default Jackson framework of Spring Boot to be effective.**

## Features

- **Annotation Marking**: Supports marking sensitive fields with annotations to simplify the handling of sensitive information in code.
- **Custom Masking Rules**: Allows users to define their own masking rules to meet different business needs.
- Supports defining masking rules via configuration files.
- Integrates with Jackson to automatically mask data during JSON serialization.

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
   <classifier>jdk17</classifier>
</dependency>
```
### 2. Non-Intrusive Configuration
   Add desensitization rule configurations in application.properties or application.yml to non-intrusively desensitize property fields. This method will be effective globally in the project.
   In the example, any property named test in any class will be desensitized after matching with a regular expression.
```yaml
   data-masking: # Masking settings
   enabled: true # Enable
   rules:  # Rules can be multiple
    - field: "test" # Property field name
      regex: "(.*?)(.{4})(.*)" # Regular expression
      replacement: "$1****$3" # Structure after replacement
```
### 2.1 For Exceptions
Use the @IgnoreSensitive annotation in the code to comment on the property to skip the global desensitization configuration.

### 3. Using Annotations
 Mark the fields that need to be masked with the @Sensitive annotation in your Java class:
 ```java
public class User {
   @Sensitive(strategy = SensitiveStrategy.EMAIL)
   private String email;

   @Sensitive(strategy = SensitiveStrategy.PHONE)
   private String phoneNumber;

   private String test;
   }
```
## Example Code
```java
public class User {
@Sensitive(type = SensitiveType.EMAIL)
private String email = "example@example.com";

    @Sensitive(type = SensitiveType.PHONE)
    private String phoneNumber = "18152484065";
    
    private String test = "testTest";

    public User(String email, String phoneNumber, String test) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.test = test;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getTest() {
        return test;
    }
    
    public void setTest(String test) {
        this.test = test;
    }
}
```
Assuming the masking rules for SensitiveStrategy.EMAIL and SensitiveStrategy.PHONE are to mask the domain part of the email and the middle four digits of the phone number, respectively, the expected JSON output might be:
```JSON
{
"email": "*******@example.com",
"phoneNumber": "181****4065"
}
```
Assuming the test property is desensitized through global non-intrusive configuration by setting the desensitization rules in the file, the expected JSON output might be:
```JSON
{
"test": "****Test"
}
```
# FAQs
## How to Customize Masking Rules?
You can define custom masking rules in the configuration file, or implement the desensitization logic by adding it to the SensitiveStrategy enumeration class and using annotations on properties.
## How to Integrate into an Existing Project?
Option 1: Use global non-intrusive configuration by setting desensitization rules in the file to achieve desensitization by default.
Option 2: Configure a small number of desensitization rules or no desensitization rules, and use annotations for property desensitization.
# Contribution
We welcome PRs and Issues to help us improve the project.
# License
This project is licensed under the MIT license. For more details, please refer to the LICENSE file.
