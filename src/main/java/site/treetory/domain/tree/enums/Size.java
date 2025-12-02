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
        // TODO: 메시지 길이에 따른 사이즈 string 반환
        return "SMALL";
    }

    public static Integer getRadius(Integer messageLength) {
        // TODO: 메시지 길이에 따른 반지름 반환
        return 1;
    }
}
