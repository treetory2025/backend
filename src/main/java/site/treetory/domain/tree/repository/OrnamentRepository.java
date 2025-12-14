package site.treetory.domain.tree.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.treetory.domain.tree.entity.Ornament;
import site.treetory.domain.tree.enums.Category;

@Repository
public interface OrnamentRepository extends JpaRepository<Ornament, Long> {

    @Query("SELECT o FROM Ornament o " +
            "LEFT JOIN PlacedOrnament p " +
            "ON p.ornament = o " +
            "WHERE (:category IS NULL OR o.category = :category) " +
            "AND (:word IS NULL OR o.name LIKE %:word%) " +
            "AND o.category != 'PRIVATE' " +
            "GROUP BY o " +
            "ORDER BY COUNT(p) DESC ")
    Page<Ornament> searchOrnaments(Category category, String word, Pageable pageable);

    Boolean existsByName(String name);
}
