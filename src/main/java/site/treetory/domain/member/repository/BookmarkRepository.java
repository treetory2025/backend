package site.treetory.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.treetory.domain.member.entity.Bookmark;
import site.treetory.domain.member.entity.BookmarkId;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {
}
