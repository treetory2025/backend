package site.treetory.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.treetory.domain.member.dto.req.ChangeNicknameReq;
import site.treetory.domain.member.dto.res.MemberDetailsRes;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.service.MemberService;
import site.treetory.global.argument_resolver.LoginMember;
import site.treetory.global.dto.ResponseDto;

import static site.treetory.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseDto<MemberDetailsRes> memberDetails(@LoginMember Member member) {

        MemberDetailsRes result = memberService.getMemberDetails(member);

        return ResponseDto.success(OK, result);
    }

    @PatchMapping("/nicknames")
    public ResponseDto<Void> changeNickname(@LoginMember Member member,
                                            @Valid @RequestBody ChangeNicknameReq changeNicknameReq) {

        memberService.changeNickname(member, changeNicknameReq);

        return ResponseDto.success(OK);
    }
}
