package site.treetory.domain.tree.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.member.repository.MemberRepository;
import site.treetory.domain.tree.dto.res.TreeDetailsRes;
import site.treetory.domain.tree.entity.PlacedOrnament;
import site.treetory.domain.tree.entity.Tree;
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
    private final PlacedOrnamentRepository placedOrnamentRepository;

    public TreeDetailsRes getTreeDetails(String uuid) {
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Tree tree = treeRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        List<PlacedOrnament> placedOrnaments = placedOrnamentRepository.findAllByTreeId(tree.getId());

        return TreeDetailsRes.toDto(member, tree, placedOrnaments);


    }
}
