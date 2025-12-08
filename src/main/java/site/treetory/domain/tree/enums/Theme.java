package site.treetory.domain.tree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.treetory.global.exception.CustomException;
import site.treetory.global.statuscode.ErrorCode;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Theme {
    SNOWY("Snowy"),
    ;

    private static final Map<String, Theme> THEME_MAP = Stream.of(values()).collect(
            Collectors.toUnmodifiableMap(t ->
                    t.description, Function.identity()));

    public static Theme getTheme(String theme) {
        return Optional.ofNullable(THEME_MAP.get(theme))
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
    }

    private final String description;
}
