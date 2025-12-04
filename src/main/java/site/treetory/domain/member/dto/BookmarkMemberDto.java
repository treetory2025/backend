package site.treetory.domain.member.dto;

import lombok.Builder;
import lombok.Data;
import site.treetory.domain.member.entity.Member;

@Data
@Builder
public class BookmarkMemberDto {

    private String memberId;

    private String nickname;

    private String email;

    public static BookmarkMemberDto toDto(Member member) {

        return BookmarkMemberDto.builder()
                .memberId(member.getUuid())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .build();
    }
}
