package site.treetory.domain.tree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.req.OrnamentListReq;
import site.treetory.domain.tree.dto.res.*;
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
    public ResponseDto<OrnamentListRes> getOrnaments(@ModelAttribute OrnamentListReq ornamentListReq) {

        OrnamentListRes result = ornamentService.getOrnaments(ornamentListReq);

        return ResponseDto.success(OK, result);
    }

    @PostMapping
    public ResponseDto<AddOrnamentRes> addOrnament(@LoginMember Member member,
                                                   @Valid @RequestBody AddOrnamentReq addOrnamentReq) {

        AddOrnamentRes result = ornamentService.addOrnament(member, addOrnamentReq);

        return ResponseDto.success(CREATED, result);
    }

    @GetMapping("/exists")
    public ResponseDto<OrnamentNameExistsRes> checkOrnamentNameExists(@RequestParam String name) {
        
        OrnamentNameExistsRes result = ornamentService.checkOrnamentNameExists(name);

        return ResponseDto.success(OK, result);
    }

    @PostMapping("/images")
    public ResponseDto<UploadImageRes> uploadImage(@LoginMember Member member,
                                                   @RequestParam("image") MultipartFile image) {

        UploadImageRes result = ornamentService.uploadImageRes(member, image);

        return ResponseDto.success(CREATED, result);
    }

    @GetMapping("/{ornamentId}")
    public ResponseDto<OrnamentDetailsRes> getOrnamentDetails(@PathVariable Long ornamentId) {
        
        OrnamentDetailsRes result = ornamentService.getOrnamentDetails(ornamentId);
        
        return ResponseDto.success(OK, result);
    }
}
