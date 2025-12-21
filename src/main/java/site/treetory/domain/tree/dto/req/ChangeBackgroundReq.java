package site.treetory.domain.tree.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeBackgroundReq {

    @NotBlank
    private String background;
}
