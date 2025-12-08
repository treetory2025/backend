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
public enum Background {
    SILENT_NIGHT,
    ;

    private static final Map<String, Background> BACKGROUND_MAP = Stream.of(values()).collect(
            Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    public static Background getBackground(String background) {
        return Optional.ofNullable(BACKGROUND_MAP.get(background))
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
    }
}
