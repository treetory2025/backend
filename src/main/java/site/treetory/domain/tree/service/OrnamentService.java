package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.treetory.domain.tree.dto.req.AddOrnamentReq;
import site.treetory.domain.tree.dto.res.OrnamentListRes;
import site.treetory.domain.tree.dto.res.OrnamentNameExistsRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.enums.Category;
import site.treetory.domain.tree.repository.OrnamentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrnamentService {

    private final OrnamentRepository ornamentRepository;
    
    public OrnamentListRes getOrnaments() {
        List<Ornament> ornaments = ornamentRepository.findAll();
        
        return OrnamentListRes.toDto(ornaments);
    }

    public void addOrnament(AddOrnamentReq req) {
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
                .isExists(exists)
                .build();
    }
}
