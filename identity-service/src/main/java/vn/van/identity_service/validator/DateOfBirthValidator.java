package vn.van.identity_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateOfBirthValidator implements ConstraintValidator<DateOfBirthConstraint, LocalDate> {
    int minAge;

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(localDate)) return true;

        long years = ChronoUnit.YEARS.between(localDate, LocalDate.now());
        return years >= minAge;
    }

    @Override
    public void initialize(DateOfBirthConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        minAge = constraintAnnotation.minAge();
    }
}
