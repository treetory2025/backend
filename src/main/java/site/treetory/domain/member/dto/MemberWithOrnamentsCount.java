package site.treetory.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberWithOrnamentsCount {

    private String memberId;

    private String nickname;

    private String email;

    private Long OrnamentsCount;
}
