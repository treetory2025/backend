package site.treetory.domain.member.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchMembersReq {

    @NotBlank
    private String query;

    public String getQuery() {
        return "%" + query.strip() + "%";
    }
}
