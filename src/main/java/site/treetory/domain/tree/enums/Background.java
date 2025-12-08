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
public enum Background {
    SILENT_NIGHT("고요한 밤"),
    ;

    private static final Map<String, Background> BACKGROUND_MAP = Stream.of(values()).collect(
            Collectors.toUnmodifiableMap(b ->
                    b.description, Function.identity()));

    public static Background getBackground(String background) {
        return Optional.ofNullable(BACKGROUND_MAP.get(background))
                .orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
    }

    private final String description;
}
