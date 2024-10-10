package com.soft.electronic.store.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
    //error message
    String message() default "Invalid Image Name !!";
    //represent group
    Class<?>[] groups() default {};
    //represent additional info about annotation
    Class<? extends Payload>[] payload() default {};
}
