package site.treetory.domain.tree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.res.OrnamentListRes;
import site.treetory.domain.tree.dto.res.OrnamentNameExistsRes;
import site.treetory.domain.tree.service.OrnamentService;
import site.treetory.global.argument_resolver.LoginMember;
import site.treetory.global.dto.ResponseDto;

import static site.treetory.global.statuscode.SuccessCode.CREATED;
import static site.treetory.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/ornaments")
@RequiredArgsConstructor
public class OrnamentController {

    private final OrnamentService ornamentService;

    @GetMapping
    public ResponseDto<OrnamentListRes> getOrnaments() {
        OrnamentListRes result = ornamentService.getOrnaments();

        return ResponseDto.success(OK, result);
    }

    @PostMapping
    public ResponseDto<Void> addOrnament(@LoginMember Member member,
                                         @Valid @RequestBody AddOrnamentReq addOrnamentReq) {
        ornamentService.addOrnament(addOrnamentReq);

        return ResponseDto.success(CREATED);
    }

    @GetMapping("/exists")
    public ResponseDto<OrnamentNameExistsRes> checkOrnamentNameExists(@RequestParam String name) {
        OrnamentNameExistsRes result = ornamentService.checkOrnamentNameExists(name);

        return ResponseDto.success(OK, result);
    }
}
