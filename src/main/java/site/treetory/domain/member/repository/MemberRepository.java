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

    @Query("SELECT NEW site.treetory.domain.member.dto.MemberWithOrnamentsCount(" +
            "   m.uuid, m.nickname, m.email, COUNT(p)) " +
            "FROM Member m " +
            "LEFT JOIN PlacedOrnament p " +
            "ON m.id = p.tree.id " +
            "WHERE m.nickname LIKE :query OR m.email LIKE :query " +
            "GROUP BY m.id " +
            "ORDER BY COUNT(p) DESC")
    Page<MemberWithOrnamentsCount> findMembersByQuery(@Param("query") String query, Pageable pageable);

    @Query("SELECT m " +
            "FROM Member m " +
            "JOIN Bookmark b " +
            "ON b.targetMember = m " +
            "WHERE b.member.id = :memberId " +
            "AND (m.nickname LIKE :query OR m.email LIKE :query) " +
            "ORDER BY b.createdAt")
    Page<Member> bookmarkList(@Param("memberId") Long memberId, @Param("query")  String query, Pageable pageable);}
