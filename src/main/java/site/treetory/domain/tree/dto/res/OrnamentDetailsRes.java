package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.global.util.DateFormatter;

@Getter
@Builder
public class OrnamentDetailsRes {

    private String name;

    private String category;

    private String imgUrl;

    private String userNickname;

    private String createdDate;

    public static OrnamentDetailsRes toDto(Ornament ornament) {
        return OrnamentDetailsRes.builder()
                .name(ornament.getName())
                .category(ornament.getCategory().name())
                .imgUrl(ornament.getImgUrl())
                .userNickname(ornament.getMember().getNickname())
                .createdDate(DateFormatter.convertToDate(ornament.getCreatedAt()))
                .build();
    }
}
