package site.treetory.domain.tree.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrnamentNameExistsRes {

    private Boolean exists;
}
