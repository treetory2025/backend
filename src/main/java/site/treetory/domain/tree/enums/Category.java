package site.treetory.domain.tree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import site.treetory.global.exception.CustomException;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static site.treetory.global.statuscode.ErrorCode.*;

@Getter
@AllArgsConstructor
public enum Category {
    CHRISTMAS("christmas"),
    FOOD("food"),
    ANIMAL("animals"),
    ETC("etc"),
    ;

    private static final Map<String, Category> CATEGORY_MAP = Stream.of(values()).collect(
            Collectors.toUnmodifiableMap(c ->
                    c.description, Function.identity()));

    public static Category getCategory(String category) {
        return Optional.ofNullable(CATEGORY_MAP.get(category))
                .orElseThrow(() -> new CustomException(BAD_REQUEST));
    }

    private final String description;
}
