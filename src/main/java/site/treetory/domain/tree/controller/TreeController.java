package site.treetory.domain.tree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.ChangeBackgroundReq;
import site.treetory.domain.tree.dto.req.ChangeThemeReq;
import site.treetory.domain.tree.dto.req.PlaceOrnamentReq;
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
    public ResponseDto<TreeDetailsRes> treeDetails(@PathVariable String uuid) {
        TreeDetailsRes result = treeService.getTreeDetails(uuid);

        return ResponseDto.success(OK, result);
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

    @PatchMapping("/theme")
    public ResponseDto<Void> changeTheme(@LoginMember Member member,
                                         @RequestBody ChangeThemeReq changeThemeReq) {

        treeService.changeTheme(member, changeThemeReq);

        return ResponseDto.success(OK);
    }

    @PatchMapping("/background")
    public ResponseDto<Void> changeBackground(@LoginMember Member member,
                                              @RequestBody ChangeBackgroundReq changeBackgroundReq) {

        treeService.changeBackground(member, changeBackgroundReq);

        return ResponseDto.success(OK);
    }
}
