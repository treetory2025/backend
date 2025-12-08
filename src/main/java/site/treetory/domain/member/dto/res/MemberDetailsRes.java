package site.treetory.domain.member.dto.res;

import lombok.Builder;
import lombok.Data;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.entity.Tree;

@Data
@Builder
public class MemberDetailsRes {

    private String nickname;

    private String email;

    private String theme;

    private String background;

    public static  MemberDetailsRes toDto(Member member, Tree tree) {

        return MemberDetailsRes.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .theme(tree.getTheme().name())
                .background(tree.getBackground().name())
                .build();
    }
}
