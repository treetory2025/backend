package site.treetory.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.treetory.domain.member.dto.res.MemberDetailsRes;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.entity.Tree;
import site.treetory.domain.tree.repository.TreeRepository;
import site.treetory.global.exception.CustomException;

import static site.treetory.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final TreeRepository treeRepository;

    public MemberDetailsRes getMemberDetails(Member member) {

        Tree tree = treeRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        return MemberDetailsRes.toDto(member, tree);
    }
}
