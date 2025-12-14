package site.treetory.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class OrnamentNameValidator implements ConstraintValidator<OrnamentNameValid, String> {

    private static final String REGEX = "^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9_]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return true;

        if (value.isEmpty() || value.length() > 10) {
            return false;
        }
        return PATTERN.matcher(value).matches();
    }
}