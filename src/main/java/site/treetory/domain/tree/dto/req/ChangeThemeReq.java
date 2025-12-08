package site.treetory.domain.tree.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeThemeReq {

    @NotBlank
    private String theme;
}
