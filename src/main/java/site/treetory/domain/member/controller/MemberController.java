package site.treetory.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import site.treetory.domain.member.dto.req.ChangeNicknameReq;
import site.treetory.domain.member.dto.req.SearchMembersReq;
import site.treetory.domain.member.dto.res.MemberDetailsRes;
import site.treetory.domain.member.dto.res.SearchMembersRes;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.service.MemberService;
import site.treetory.global.argument_resolver.LoginMember;
import site.treetory.global.dto.ResponseDto;

import static site.treetory.global.statuscode.SuccessCode.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseDto<MemberDetailsRes> memberDetails(@LoginMember Member member) {

        MemberDetailsRes result = memberService.getMemberDetails(member);

        return ResponseDto.success(OK, result);
    }

    @GetMapping
    public ResponseDto<SearchMembersRes> searchMembers(@ModelAttribute @Valid SearchMembersReq searchMembersReq,
                                                       @PageableDefault Pageable pageable) {

        SearchMembersRes result = memberService.searchMembers(searchMembersReq, pageable);

        return ResponseDto.success(OK, result);
    }

    @PatchMapping("/nicknames")
    public ResponseDto<Void> changeNickname(@LoginMember Member member,
                                            @Valid @RequestBody ChangeNicknameReq changeNicknameReq) {

        memberService.changeNickname(member, changeNicknameReq);

        return ResponseDto.success(OK);
    }

    @PostMapping("/bookmarks/{targetMemberId}")
    public ResponseDto<Void> addBookmark(@LoginMember Member member, @PathVariable String targetMemberId) {

        memberService.addBookmark(member, targetMemberId);

        return ResponseDto.success(CREATED);
    }

    @DeleteMapping("/bookmarks/{targetMemberId}")
    public ResponseDto<Void> deleteBookmark(@LoginMember Member member, @PathVariable String targetMemberId) {

        memberService.deleteBookmark(member, targetMemberId);

        return ResponseDto.success(NO_CONTENT);
    }
}
