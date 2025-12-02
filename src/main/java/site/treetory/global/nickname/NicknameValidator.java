package site.treetory.global.nickname;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<NicknameValid, String> {

    private static final String REGEX = "^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9\\u3040-\\u309F\\u30A0-\\u30FF !@#$%&*()^._-]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank() || value.length() > 6) {
            return false;
        }
        return PATTERN.matcher(value).matches();
    }
}