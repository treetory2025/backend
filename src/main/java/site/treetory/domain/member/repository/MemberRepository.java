package site.treetory.domain.member.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.treetory.domain.member.dto.MemberWithOrnamentsCount;
import site.treetory.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findByEmail(String email);

    @Query("select new site.treetory.domain.member.dto.MemberWithOrnamentsCount(" +
            "   m.uuid, m.nickname, m.email, count(p)) " +
            "from Member m " +
            "left join PlacedOrnament p " +
            "on m.id = p.tree.id " +
            "where m.nickname like :query or m.email like :query " +
            "group by m.id " +
            "order by count(p) desc")
    Page<MemberWithOrnamentsCount> findMembersByQuery(@Param("query") String query, Pageable pageable);

    @Query("select m " +
            "from Member m " +
            "join Bookmark b " +
            "on b.targetMember = m " +
            "where b.member.id = :memberId " +
            "and (m.nickname like :query or m.email like :query) " +
            "order by b.createdAt")
    Page<Member> bookmarkList(@Param("memberId") Long memberId, @Param("query")  String query, Pageable pageable);
}
