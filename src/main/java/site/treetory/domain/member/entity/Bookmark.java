package site.treetory.domain.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", columnDefinition = "INT UNSIGNED", nullable = false)
    private Member member;

    @ManyToOne(fetch = LAZY, optional = false)
    @MapsId("targetMemberId")
    @JoinColumn(name = "target_member_id", columnDefinition = "INT UNSIGNED", nullable = false)
    private Member targetMember;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
