package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.entity.Tree;

import java.util.List;

@Getter
@Builder
public class TreeDetailsRes {

    private String nickname;

    private Boolean isBookmarked;

    private Integer treeSize;

    private String treeTheme;

    private String treeBackground;

    private List<TreeDetailsOrnamentsRes> ornamentsRes;

    public static TreeDetailsRes toDto(Tree tree, List<PlacedOrnament> placedOrnaments, Boolean isBookmarked) {
        return TreeDetailsRes.builder()
                .nickname(tree.getMember().getNickname())
                .isBookmarked(isBookmarked)
                .treeSize(tree.getSize())
                .treeTheme(tree.getTheme().name())
                .treeBackground(tree.getBackground().name())
                .ornamentsRes(TreeDetailsOrnamentsRes.toDto(placedOrnaments))
                .build();
    }
}
