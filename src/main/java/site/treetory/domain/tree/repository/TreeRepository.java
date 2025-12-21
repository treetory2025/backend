package site.treetory.domain.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import site.treetory.domain.member.entity.Member;
import site.treetory.domain.tree.entity.Tree;

import java.util.Optional;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {

    Optional<Tree> findByMember(Member member);

    @Query("SELECT t FROM Tree t JOIN FETCH t.member m WHERE m.uuid = :uuid")
    Optional<Tree> findByUuid(@Param("uuid") String uuid);
}
