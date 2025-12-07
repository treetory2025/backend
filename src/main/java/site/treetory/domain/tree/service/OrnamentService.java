package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.res.OrnamentDetailsRes;
import site.treetory.domain.tree.dto.res.OrnamentListRes;
import site.treetory.domain.tree.dto.res.OrnamentNameExistsRes;
import site.treetory.domain.tree.dto.res.UploadImageRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.enums.Category;
import site.treetory.domain.tree.repository.OrnamentRepository;
import site.treetory.global.exception.CustomException;
import site.treetory.global.util.S3Uploader;

import static site.treetory.global.statuscode.ErrorCode.BAD_REQUEST;
import static site.treetory.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrnamentService {

    private final OrnamentRepository ornamentRepository;
    private final S3Uploader s3Uploader;

    public OrnamentListRes getOrnaments(String category, Pageable pageable) {

        Page<Ornament> ornaments;
        if (category == null) {
            ornaments = ornamentRepository.findAll(pageable);
        } else {
            ornaments = ornamentRepository.findAllByCategory(Category.getCategory(category), pageable);
        }

        return OrnamentListRes.toDto(ornaments);
    }

    public void addOrnament(Member member, AddOrnamentReq req) {

        if (ornamentRepository.existsByName(req.getName())) {
            throw new CustomException(BAD_REQUEST);
        }

        Ornament ornament = Ornament.builder()
                .member(member)
                .name(req.getName())
                .category(Category.getCategory(req.getCategory()))
                .imgUrl(req.getImgUrl())
                .isPublic(req.getIsPublic())
                .build();

        ornamentRepository.save(ornament);
    }

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

        return OrnamentDetailsRes.toDto(ornament);
    }
}
