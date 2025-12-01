package site.treetory.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
