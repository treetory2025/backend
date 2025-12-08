package site.treetory.domain.tree.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.req.OrnamentListReq;
import site.treetory.domain.tree.dto.res.OrnamentDetailsRes;
import site.treetory.domain.tree.dto.res.OrnamentListRes;
import site.treetory.domain.tree.dto.res.OrnamentNameExistsRes;
import site.treetory.domain.tree.dto.res.UploadImageRes;
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

    private final int ORNAMENT_PAGE_SIZE = 18;
    private final String ORNAMENT_SORT_KEY = "createdAt";

    @GetMapping
    public ResponseDto<OrnamentListRes> getOrnaments(@ModelAttribute OrnamentListReq ornamentListReq,
                                                     @RequestParam(required = false, defaultValue = "0") int page) {
        
        page = page < 0 ? 0 : page;

        PageRequest pageable = PageRequest.of(page,
                ORNAMENT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, ORNAMENT_SORT_KEY)
        );

        OrnamentListRes result = ornamentService.getOrnaments(ornamentListReq, pageable);

        return ResponseDto.success(OK, result);
    }

    @PostMapping
    public ResponseDto<Void> addOrnament(@LoginMember Member member,
                                         @Valid @RequestBody AddOrnamentReq addOrnamentReq) {
        
        ornamentService.addOrnament(member, addOrnamentReq);

        return ResponseDto.success(CREATED);
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
