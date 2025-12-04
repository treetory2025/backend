package site.treetory.domain.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.treetory.domain.tree.entity.PlacedOrnament;

import java.util.List;

@Repository
public interface PlacedOrnamentRepository extends JpaRepository<PlacedOrnament, Long> {
    
    List<PlacedOrnament> findAllByTreeId(Long treeId);
}
