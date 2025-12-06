package site.treetory.domain.tree.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.enums.Category;

@Repository
public interface OrnamentRepository extends JpaRepository<Ornament, Long> {

    Page<Ornament> findAllByCategory(Category category, Pageable pageable);
    
    Boolean existsByName(String name);
}
