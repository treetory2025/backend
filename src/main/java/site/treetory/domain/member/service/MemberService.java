package site.treetory.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.treetory.domain.member.dto.MemberWithOrnamentsCount;
import site.treetory.domain.member.dto.req.ChangeNicknameReq;
import site.treetory.domain.member.dto.req.SearchMembersReq;
import site.treetory.domain.member.dto.res.MemberDetailsRes;
import site.treetory.domain.member.dto.res.SearchMembersRes;
import site.treetory.domain.member.entity.Bookmark;
import site.treetory.domain.member.entity.BookmarkId;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.repository.BookmarkRepository;
import site.treetory.domain.member.repository.MemberRepository;
import site.treetory.domain.tree.entity.Tree;
import site.treetory.domain.tree.repository.TreeRepository;
import site.treetory.global.exception.CustomException;

import static site.treetory.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TreeRepository treeRepository;
    private final BookmarkRepository bookmarkRepository;

    public MemberDetailsRes getMemberDetails(Member member) {

        Tree tree = treeRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        return MemberDetailsRes.toDto(member, tree);
    }

    public SearchMembersRes searchMembers(SearchMembersReq searchMembersReq, Pageable pageable) {

        String query = searchMembersReq.getQuery();

        Page<MemberWithOrnamentsCount> members = memberRepository.findMembersByQuery(query, pageable);

        return new SearchMembersRes(members);
    }

    @Transactional
    public void changeNickname(Member member, ChangeNicknameReq changeNicknameReq) {

        member.changeNickname(changeNicknameReq.getNickname());

        memberRepository.save(member);
    }

    @Transactional
    public void addBookmark(Member member, String targetMemberId) {

        Member targetMember = memberRepository.findByUuid(targetMemberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Bookmark bookmark = createBookmark(member, targetMember);

        bookmarkRepository.save(bookmark);
    }

    private Bookmark createBookmark(Member member, Member targetMember) {

        BookmarkId bookmarkId = new BookmarkId(member.getId(), targetMember.getId());

        return Bookmark.builder()
                .id(bookmarkId)
                .member(member)
                .targetMember(targetMember)
                .build();
    }

    @Transactional
    public void deleteBookmark(Member member, String targetMemberId) {

        Member targetMember = memberRepository.findByUuid(targetMemberId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        BookmarkId bookmarkId = new BookmarkId(member.getId(), targetMember.getId());

        bookmarkRepository.deleteById(bookmarkId);
    }
}
