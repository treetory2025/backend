package site.treetory.domain.tree.dto.req;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrnamentListReq {
    
    private final int ORNAMENT_PAGE_SIZE = 18;

    private String category;

    private String word;

    private Integer page = 0;
    
    public Pageable getPageable() {
        page = page < 0 ? 0 : page;

        return PageRequest.of(page,
                ORNAMENT_PAGE_SIZE
        );
    }
}
