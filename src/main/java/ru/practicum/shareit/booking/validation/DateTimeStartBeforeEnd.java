package ru.practicum.shareit.booking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateTimeStartBeforeEndValidator.class)
public @interface DateTimeStartBeforeEnd {
    String message() default "{ru.practicum.shareit.booking.validation - start date-time must be before end}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
