package com.wardyn.Projekt2.validators;

import com.wardyn.Projekt2.validatorInterfaces.ValidDomain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DomainValidator implements ConstraintValidator<ValidDomain, String> {
    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        if (code.length() < 3) {
            return false;
        }

        String[] codeList = code.split("\\.");

        if (codeList.length == 2) {
            return codeList[0].length() > 0 && codeList[1].length() > 0;
        }
       return false;
    }
}
