package site.treetory.domain.tree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Background {
    SILENT_NIGHT("고요한 밤"),
    ;

    private final String description;
}
