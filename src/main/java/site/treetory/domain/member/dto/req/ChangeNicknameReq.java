package site.treetory.domain.member.dto.req;

import lombok.Data;
import site.treetory.global.nickname.NicknameValid;

@Data
public class ChangeNicknameReq {

    @NicknameValid
    private String nickname;
}
