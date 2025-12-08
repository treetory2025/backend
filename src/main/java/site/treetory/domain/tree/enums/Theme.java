package site.treetory.domain.tree.enums;

import lombok.Getter;
import site.treetory.global.exception.CustomException;
import site.treetory.global.statuscode.ErrorCode;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Theme {
    SNOWY,
    ;

    private static final Map<String, Theme> THEME_MAP = Stream.of(values()).collect(
            Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    public static Theme getTheme(String theme) {
        return Optional.ofNullable(THEME_MAP.get(theme))
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
    }
}
