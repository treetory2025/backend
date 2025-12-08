package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.dto.req.ChangeBackgroundReq;
import site.treetory.domain.tree.dto.req.ChangeThemeReq;
import site.treetory.domain.tree.dto.req.PlaceOrnamentReq;
import site.treetory.domain.tree.dto.res.TreeDetailsRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.entity.Tree;
import site.treetory.domain.tree.enums.Background;
import site.treetory.domain.tree.enums.Font;
import site.treetory.domain.tree.enums.Theme;
import site.treetory.domain.tree.repository.OrnamentRepository;
import site.treetory.domain.tree.repository.PlacedOrnamentRepository;
import site.treetory.domain.tree.repository.TreeRepository;
import site.treetory.global.exception.CustomException;

import java.util.List;

import static site.treetory.global.statuscode.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreeService {

    private final TreeRepository treeRepository;
    private final OrnamentRepository ornamentRepository;
    private final PlacedOrnamentRepository placedOrnamentRepository;

    @Transactional(readOnly = true)
    public TreeDetailsRes getTreeDetails(String uuid) {

        Tree tree = treeRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        List<PlacedOrnament> placedOrnaments = placedOrnamentRepository.findAllByTreeId(tree.getId());

        return TreeDetailsRes.toDto(tree, placedOrnaments);
    }
    
    @Transactional
    public void resizeTree(Member member) {
        
        Tree tree = treeRepository.findByMember(member)
                .orElseThrow(() -> new CustomException(NOT_FOUND));
        
        tree.resize();
    }

    @Transactional
    public void placeOrnament(String uuid, PlaceOrnamentReq req) {

        Tree tree = treeRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Ornament ornament = ornamentRepository.findById(req.getOrnamentId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        PlacedOrnament placedOrnament = PlacedOrnament.builder()
                .tree(tree)
                .ornament(ornament)
                .positionX(req.getPositionX())
                .positionY(req.getPositionY())
                .message(req.getMessage())
                .writerNickname(req.getNickname())
                .font(Font.getFont(req.getFont()))
                .build();

        placedOrnamentRepository.save(placedOrnament);
    }

    @Transactional
    public void deleteOrnament(Member member, Long placedOrnamentId) {

        placedOrnamentRepository.findById(placedOrnamentId)
                .ifPresentOrElse(placedOrnament -> {
                            validateOwner(member, placedOrnament);
                            placedOrnamentRepository.delete(placedOrnament);
                        }, () -> log.warn("삭제하려는 장식을 찾을 수 없습니다. ID: {}", placedOrnamentId)
                );
    }

    private void validateOwner(Member member, PlacedOrnament placedOrnament) {

        if (!member.getId().equals(placedOrnament.getTree().getId())) {
            throw new CustomException(FORBIDDEN);
        }
    }

    @Transactional
    public void changeTheme(Member member, ChangeThemeReq req) {

        Tree tree = treeRepository.findByUuid(member.getUuid())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Theme theme = Theme.getTheme(req.getTheme());

        tree.changeTheme(theme);

        treeRepository.save(tree);
    }

    @Transactional
    public void changeBackground(Member member, ChangeBackgroundReq req) {

        Tree tree = treeRepository.findByUuid(member.getUuid())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Background background = Background.getBackground(req.getBackground());

        tree.changeBackground(background);

        treeRepository.save(tree);
    }
}
