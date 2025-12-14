package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.req.OrnamentListReq;
import site.treetory.domain.tree.dto.res.OrnamentDetailsRes;
import site.treetory.domain.tree.dto.res.OrnamentListRes;
import site.treetory.domain.tree.dto.res.OrnamentNameExistsRes;
import site.treetory.domain.tree.dto.res.UploadImageRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.enums.Category;
import site.treetory.domain.tree.repository.OrnamentRepository;
import site.treetory.global.exception.CustomException;
import site.treetory.global.util.S3Uploader;

import static site.treetory.domain.tree.enums.Category.PRIVATE;
import static site.treetory.global.statuscode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class OrnamentService {

    private final OrnamentRepository ornamentRepository;
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public OrnamentListRes getOrnaments(OrnamentListReq req) {

        Category category = req.getCategory() != null ? Category.getCategory(req.getCategory()) : null;

        Page<Ornament> ornaments = ornamentRepository.searchOrnaments(category, req.getWord(), req.getPageable());

        return OrnamentListRes.toDto(ornaments);
    }

    @Transactional
    public void addOrnament(Member member, AddOrnamentReq req) {

        Category category = Category.getCategory(req.getCategory());

        validateCategoryAndName(req, category);

        Ornament ornament = Ornament.builder()
                .member(member)
                .name(req.getName())
                .category(category)
                .imgUrl(req.getImgUrl())
                .build();

        ornamentRepository.save(ornament);
    }

    private void validateCategoryAndName(AddOrnamentReq req, Category category) {

        if (category == PRIVATE && req.getName() != null) {
            throw new CustomException(BAD_REQUEST);
        }

        if (category != PRIVATE) {
            if (req.getName() == null || ornamentRepository.existsByName(req.getName())) {
                throw new CustomException(BAD_REQUEST);
            }
        }
    }

    @Transactional(readOnly = true)
    public OrnamentNameExistsRes checkOrnamentNameExists(String name) {

        Boolean exists = ornamentRepository.existsByName(name);

        return OrnamentNameExistsRes.builder()
                .exists(exists)
                .build();
    }

    public UploadImageRes uploadImageRes(Member member, MultipartFile image) {

        String dirName = "members/" + member.getUuid() + "/ornaments";

        String imageUrl = s3Uploader.upload(member.getId(), image, dirName);

        return new UploadImageRes(imageUrl);
    }

    public OrnamentDetailsRes getOrnamentDetails(Long ornamentId) {

        Ornament ornament = ornamentRepository.findById(ornamentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        if (ornament.getCategory() == PRIVATE) {
            throw new CustomException(FORBIDDEN);
        }

        return OrnamentDetailsRes.toDto(ornament);
    }
}
