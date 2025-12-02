package site.treetory.domain.tree.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.treetory.domain.tree.dto.res.TreeDetailsRes;
import site.treetory.domain.tree.service.TreeService;
import site.treetory.global.dto.ResponseDto;

import static site.treetory.global.statuscode.SuccessCode.OK;

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
}
