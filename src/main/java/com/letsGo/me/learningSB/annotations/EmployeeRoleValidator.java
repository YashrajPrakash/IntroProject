package com.letsGo.me.learningSB.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EmployeeRoleValidator implements ConstraintValidator<EmployeeRoleValidation, String> {
    @Override
    public boolean isValid(String inputRoles, ConstraintValidatorContext constraintValidatorContext) {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        roles.add("ADMIN");
        return roles.contains(inputRoles);
    }
}
