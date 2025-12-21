package site.treetory.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class ImgUrlValidator implements ConstraintValidator<ImgUrlValid, String> {

    @Value("${spring.cloud.aws.s3.url}")
    private String imageUrlPrefix;

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {

        if (url == null || url.isBlank()) {
            return false;
        }

        if (!url.startsWith(imageUrlPrefix)) {
            return false;
        }

        try {
            new URI(url).toURL();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
