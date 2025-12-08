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
public enum Font {
    NANUM_PEN,
    ;

    private static final Map<String, Font> FONT_MAP = Stream.of(values()).collect(
            Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    public static Font getFont(String font) {
        return Optional.ofNullable(FONT_MAP.get(font))
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
    }
}
