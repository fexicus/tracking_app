package diploma.trackingApp.config;

import diploma.trackingApp.util.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default "Пароль должен содержать как минимум одну букву и одну цифру";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}