package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.res.OrnamentListRes;
import site.treetory.domain.tree.dto.res.OrnamentNameExistsRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.enums.Category;
import site.treetory.domain.tree.repository.OrnamentRepository;
import site.treetory.global.exception.CustomException;

import static site.treetory.global.statuscode.ErrorCode.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class OrnamentService {

    private final OrnamentRepository ornamentRepository;

    public OrnamentListRes getOrnaments(String category, Pageable pageable) {

        Page<Ornament> ornaments;
        if (category == null) {
            ornaments = ornamentRepository.findAll(pageable);
        } else {
            ornaments = ornamentRepository.findAllByCategory(Category.getCategory(category), pageable);
        }

        return OrnamentListRes.toDto(ornaments);
    }

    public void addOrnament(AddOrnamentReq req) {
        if (ornamentRepository.existsByName(req.getName())) {
            throw new CustomException(BAD_REQUEST);
        }
        
        Ornament ornament = Ornament.builder()
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
}
