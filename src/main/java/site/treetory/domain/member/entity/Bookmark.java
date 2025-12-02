package site.treetory.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @EmbeddedId
    private BookmarkId bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", columnDefinition = "INT UNSIGNED", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("targetMemberId")
    @JoinColumn(name = "target_member_id", columnDefinition = "INT UNSIGNED", nullable = false)
    private Member targetMember;
}
