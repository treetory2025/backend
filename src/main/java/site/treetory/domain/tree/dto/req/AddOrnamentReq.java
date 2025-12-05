package site.treetory.domain.tree.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddOrnamentReq {

    @NotBlank
    @Length(max = 10)
    private String name;

    @NotNull
    private String category;

    @NotNull
    @Length(max = 255)
    private String imgUrl;

    @NotNull
    private Boolean isPublic;
}
