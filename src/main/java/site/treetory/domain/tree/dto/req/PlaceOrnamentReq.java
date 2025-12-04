package site.treetory.domain.tree.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.treetory.global.nickname.NicknameValid;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrnamentReq {

    private Long ornamentId;

    @NicknameValid
    private String nickname;

    @NotNull
    private String message;
    
    private Integer positionX;
    
    private Integer positionY;

    private String font;
    
}
