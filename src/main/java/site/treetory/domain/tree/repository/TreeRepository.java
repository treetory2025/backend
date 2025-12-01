package site.treetory.domain.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.treetory.domain.tree.entity.Tree;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {
}
