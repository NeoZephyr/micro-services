## 控制器接口参数校验
```
@PhoneNumber
@Min
@NotBlank
@Valid
```

## DTO 参数校验
```java
public class AccountDto {
    @NotBlank
    private String id;

    @Email(message = "Invalid email")
    private String email;

    @NotNull
    private Date joinDate;

    @PhoneNumber
    private String phoneNumber;

    @NotEmpty
    private String avatarUrl;
}
```

## 自定义标注
```java
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";
    Class[] groups() default {};
    Class[] payload() default {};
}
```
```java
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return true;
        }
        return phoneField != null && phoneField.matches("[0-9]+")
                && (phoneField.length() > 8) && (phoneField.length() < 14);
    }
}
```
