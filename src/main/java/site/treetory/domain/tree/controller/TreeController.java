package site.treetory.domain.tree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.ChangeBackgroundReq;
import site.treetory.domain.tree.dto.req.ChangeThemeReq;
import site.treetory.domain.tree.dto.req.PlaceOrnamentReq;
import site.treetory.domain.tree.dto.res.MessageDetailsRes;
import site.treetory.domain.tree.dto.res.TreeDetailsRes;
import site.treetory.domain.tree.service.TreeService;
import site.treetory.global.argument_resolver.LoginMember;
import site.treetory.global.dto.ResponseDto;

import static site.treetory.global.statuscode.SuccessCode.*;

@RestController
@RequestMapping("/api/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;

    @GetMapping("/{uuid}")
    public ResponseDto<TreeDetailsRes> treeDetails(@LoginMember Member member,
                                                   @PathVariable String uuid) {

        TreeDetailsRes result = treeService.getTreeDetails(uuid, member);

        return ResponseDto.success(OK, result);
    }

    @PatchMapping("/size")
    public ResponseDto<Void> resizeTree(@LoginMember Member member) {

        treeService.resizeTree(member);

        return ResponseDto.success(OK);
    }

    @PostMapping("/{uuid}/ornaments")
    public ResponseDto<Void> placeOrnament(@PathVariable String uuid,
                                           @Valid @RequestBody PlaceOrnamentReq placeOrnamentReq) {

        treeService.placeOrnament(uuid, placeOrnamentReq);

        return ResponseDto.success(CREATED);
    }

    @DeleteMapping("/ornaments/{placedOrnamentId}")
    public ResponseDto<Void> deleteOrnament(@LoginMember Member member,
                                            @PathVariable Long placedOrnamentId) {

        treeService.deleteOrnament(member, placedOrnamentId);

        return ResponseDto.success(NO_CONTENT);
    }

    @PatchMapping("/themes")
    public ResponseDto<Void> changeTheme(@LoginMember Member member,
                                         @RequestBody ChangeThemeReq changeThemeReq) {

        treeService.changeTheme(member, changeThemeReq);

        return ResponseDto.success(OK);
    }

    @PatchMapping("/backgrounds")
    public ResponseDto<Void> changeBackground(@LoginMember Member member,
                                              @RequestBody ChangeBackgroundReq changeBackgroundReq) {

        treeService.changeBackground(member, changeBackgroundReq);

        return ResponseDto.success(OK);
    }

    @GetMapping("/messages/{placedOrnamentId}")
    public ResponseDto<MessageDetailsRes> messageDetails(@LoginMember Member member,
                                                         @PathVariable Long placedOrnamentId) {

        MessageDetailsRes result = treeService.messageDetails(member, placedOrnamentId);

        return ResponseDto.success(OK, result);
    }
}
