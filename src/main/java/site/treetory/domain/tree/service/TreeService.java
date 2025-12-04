package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.repository.MemberRepository;
import site.treetory.domain.tree.dto.req.PlaceOrnamentReq;
import site.treetory.domain.tree.dto.res.TreeDetailsRes;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.entity.Tree;
import site.treetory.domain.tree.enums.Font;
import site.treetory.domain.tree.repository.OrnamentRepository;
import site.treetory.domain.tree.repository.PlacedOrnamentRepository;
import site.treetory.domain.tree.repository.TreeRepository;
import site.treetory.global.exception.CustomException;

import java.util.List;

import static site.treetory.global.statuscode.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class TreeService {

    private final MemberRepository memberRepository;
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
                .orElseThrow(() -> new CustomException(NOT_FOUND))
                .delete(member);
    }
}
