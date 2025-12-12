package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.enums.Size;
import site.treetory.global.util.DateFormatter;

import java.util.List;

@Getter
@Builder
public class TreeDetailsOrnamentsRes {

    private Long placedOrnamentId;

    private Long ornamentId;

    private String writerNickname;

    private Integer positionX;

    private Integer positionY;

    private String size;

    private String imgUrl;

    private String createdDate;

    public static TreeDetailsOrnamentsRes toDto(PlacedOrnament placedOrnament) {
        return TreeDetailsOrnamentsRes.builder()
                .placedOrnamentId(placedOrnament.getId())
                .ornamentId(placedOrnament.getOrnament().getId())
                .writerNickname(placedOrnament.getWriterNickname())
                .positionX(placedOrnament.getPositionX())
                .positionY(placedOrnament.getPositionY())
                .size(Size.getSize(placedOrnament.getMessage().length()))
                .imgUrl(placedOrnament.getOrnament().getImgUrl())
                .createdDate(DateFormatter.convertToDate(placedOrnament.getCreatedAt()))
                .build();
    }

    public static List<TreeDetailsOrnamentsRes> toDto(List<PlacedOrnament> placedOrnaments) {
        return placedOrnaments.stream().map(TreeDetailsOrnamentsRes::toDto).toList();
    }

}
