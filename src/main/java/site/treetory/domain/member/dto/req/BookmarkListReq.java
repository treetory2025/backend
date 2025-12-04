package site.treetory.domain.member.dto.req;

import lombok.Data;

@Data
public class BookmarkListReq {

    private String query = "";

    public String getQuery() {
        return "%" + query.strip() + "%";
    }
}
