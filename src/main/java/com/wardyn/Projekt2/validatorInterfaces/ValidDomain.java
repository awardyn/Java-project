package com.wardyn.Projekt2.validatorInterfaces;

import com.wardyn.Projekt2.validators.DomainValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DomainValidator.class)
public @interface ValidDomain {
    String message() default "This string must fit x...y.a...b style (for example: onet.pl, google.com)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
