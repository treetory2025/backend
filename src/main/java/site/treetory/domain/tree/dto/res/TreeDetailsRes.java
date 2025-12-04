package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.entity.Tree;

import java.util.List;

@Getter
@Builder
public class TreeDetailsRes {

    private String nickname;

    private Integer treeSize;

    private String treeTheme;

    private String treeBackground;

    private List<TreeDetailsOrnamentsRes> ornamentsRes;

    public static TreeDetailsRes toDto(Tree tree, List<PlacedOrnament> placedOrnaments) {
        return TreeDetailsRes.builder()
                .nickname(tree.getMember().getNickname())
                .treeSize(tree.getSize())
                .treeTheme(tree.getTheme().getDescription())
                .treeBackground(tree.getBackground().getDescription())
                .ornamentsRes(TreeDetailsOrnamentsRes.toDto(placedOrnaments))
                .build();
    }

}
