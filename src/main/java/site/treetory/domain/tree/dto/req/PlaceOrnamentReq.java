package site.treetory.domain.tree.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import site.treetory.global.validator.NicknameValid;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrnamentReq {

    @NotNull
    private Long ornamentId;

    @NicknameValid
    private String nickname;

    @NotBlank
    @Length(max = 300)
    private String message;
    
    @NotNull
    private Integer positionX;
    
    @NotNull
    private Integer positionY;

    @NotNull
    private String font;
}
