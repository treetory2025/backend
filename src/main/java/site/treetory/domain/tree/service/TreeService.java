package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.repository.BookmarkRepository;
import site.treetory.domain.tree.dto.req.ChangeBackgroundReq;
import site.treetory.domain.tree.dto.req.ChangeThemeReq;
import site.treetory.domain.tree.dto.req.PlaceOrnamentReq;
import site.treetory.domain.tree.dto.res.MessageDetailsRes;
import site.treetory.domain.tree.dto.res.TreeDetailsRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.entity.Tree;
import site.treetory.domain.tree.enums.Background;
import site.treetory.domain.tree.enums.Font;
import site.treetory.domain.tree.enums.Size;
import site.treetory.domain.tree.enums.Theme;
import site.treetory.domain.tree.repository.OrnamentRepository;
import site.treetory.domain.tree.repository.PlacedOrnamentRepository;
import site.treetory.domain.tree.repository.TreeRepository;
import site.treetory.global.exception.CustomException;

import java.time.LocalDateTime;
import java.util.List;

import static site.treetory.global.statuscode.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TreeService {

    private final TreeRepository treeRepository;
    private final OrnamentRepository ornamentRepository;
    private final PlacedOrnamentRepository placedOrnamentRepository;
    private final BookmarkRepository bookmarkRepository;

    @Transactional(readOnly = true)
    public TreeDetailsRes getTreeDetails(String uuid, Member member) {

        Tree tree = treeRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        List<PlacedOrnament> placedOrnaments = placedOrnamentRepository.findAllByTreeId(tree.getId());

        Boolean isBookmarked = bookmarkRepository.existsByMemberAndTargetMember(member, tree.getMember());

        return TreeDetailsRes.toDto(tree, placedOrnaments, isBookmarked);
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

        List<PlacedOrnament> placedOrnaments = placedOrnamentRepository.findAllByTreeId(tree.getId());

        PlacedOrnament placedOrnament = PlacedOrnament.builder()
                .tree(tree)
                .ornament(ornament)
                .positionX(req.getPositionX())
                .positionY(req.getPositionY())
                .message(req.getMessage())
                .writerNickname(req.getNickname())
                .font(Font.getFont(req.getFont()))
                .build();

        if (checkIntersection(placedOrnament, placedOrnaments)) {
            throw new CustomException(BAD_REQUEST);
        }

        placedOrnamentRepository.save(placedOrnament);
    }

    private boolean checkIntersection(PlacedOrnament placedOrnament, List<PlacedOrnament> placedOrnaments) {

        for (PlacedOrnament ornament : placedOrnaments) {
            if (checkIntersection(ornament, placedOrnament)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkIntersection(PlacedOrnament o1, PlacedOrnament o2) {

        int dx = o1.getPositionX() - o2.getPositionX();
        int dy = o1.getPositionY() - o2.getPositionY();

        int r1 = Size.getRadius(o1.getMessage().length());
        int r2 = Size.getRadius(o2.getMessage().length());
        int radSum = r1 + r2;

        return dx * dx + dy * dy < radSum * radSum;

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

    @Transactional(readOnly = true)
    public MessageDetailsRes messageDetails(Member member, Long placedOrnamentId) {

        if (LocalDateTime.now().isBefore(LocalDateTime.parse("2025-12-25T00:00:00"))) {
            throw new CustomException(FORBIDDEN);
        }

        PlacedOrnament placedOrnament = placedOrnamentRepository.findById(placedOrnamentId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        if (!placedOrnament.getTree().getId().equals(member.getId())) {
            throw new CustomException(FORBIDDEN);
        }

        return MessageDetailsRes.toDto(placedOrnament);
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
