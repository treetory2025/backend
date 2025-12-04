package site.treetory.domain.member.dto.res;

import lombok.Data;
import org.springframework.data.domain.Page;
import site.treetory.domain.member.dto.BookmarkMemberDto;
import site.treetory.domain.member.entity.Member;
import site.treetory.global.dto.PageDto;

@Data
public class BookmarkListRes {

    private PageDto<BookmarkMemberDto> members;

    public BookmarkListRes(Page<Member> members) {
        this.members = new PageDto<>(members.map(BookmarkMemberDto::toDto));
    }
}
