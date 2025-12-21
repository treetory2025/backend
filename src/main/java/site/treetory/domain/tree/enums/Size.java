package site.treetory.domain.tree.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Size {
    SMALL(1),
    MEDIUM(2),
    LARGE(3),
    ;

    private final Integer radius;

    public static String getSize(Integer messageLength) {

        if (messageLength < 100) {
            return "SMALL";
        } else if (messageLength < 200) {
            return "MEDIUM";
        } else {
            return "LARGE";
        }
    }

    public static int getRadius(Integer messageLength) {

        if (messageLength < 100) {
            return 22;
        } else if (messageLength < 200) {
            return 30;
        } else {
            return 38;
        }
    }
}
