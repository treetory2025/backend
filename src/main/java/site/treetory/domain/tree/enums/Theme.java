package site.treetory.domain.tree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Theme {
    SNOWY("눈 덮인 트리"),
    ;

    private final String description;
}
