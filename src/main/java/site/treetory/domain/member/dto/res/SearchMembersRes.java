package site.treetory.domain.member.dto.res;

import lombok.Data;
import org.springframework.data.domain.Page;
import site.treetory.domain.member.dto.MemberWithOrnamentsCount;
import site.treetory.global.dto.PageDto;

@Data
public class SearchMembersRes {

    private PageDto<MemberWithOrnamentsCount> members;

    public SearchMembersRes(Page<MemberWithOrnamentsCount> members) {
        this.members = new PageDto<>(members);
    }
}
