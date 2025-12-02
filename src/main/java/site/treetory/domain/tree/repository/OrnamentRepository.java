package site.treetory.domain.tree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.treetory.domain.tree.entity.Ornament;

@Repository
public interface OrnamentRepository extends JpaRepository<Ornament, Long> {
}
