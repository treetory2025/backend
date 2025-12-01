package site.treetory.domain.tree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Size {
    SMALL("소"),
    MEDIUM("중"),
    LARGE("대"),
    ;


    private final String description;

}
